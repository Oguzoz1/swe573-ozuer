package com.studyhole.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.studyhole.app.data.CommunityPackage;
import com.studyhole.app.data.ImagePackage;
import com.studyhole.app.data.UserPackage;
import com.studyhole.app.mapper.CommunityMapper;
import com.studyhole.app.mapper.ImageMapper;
import com.studyhole.app.mapper.UserMapper;
import com.studyhole.app.model.Community;
import com.studyhole.app.model.User;
import com.studyhole.app.model.DataTypes.Image;
import com.studyhole.app.model.Post.Post;
import com.studyhole.app.repository.CommunityRepository;
import com.studyhole.app.repository.ImageRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
@AllArgsConstructor
public class CommunityService  {

    private final CommunityMapper communityMapper;
    private final UserMapper userMapper;
    private final CommunityRepository communityRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    //Services
    private final StudyholeService studyholeService;
    
    //Do not forget to add transactional to secure consistency (databse related)
    @Transactional
    public CommunityPackage save(CommunityPackage communityPackage){
        User userdetails = studyholeService.getCurrentUser();
        if (communityPackage.getMemberIds() == null){
            communityPackage.setMemberIds(new ArrayList<>());
        }
        if (communityPackage.getAppliedMemberIds() == null){
            communityPackage.setAppliedMemberIds(new ArrayList<>());
        }
        communityPackage.getMemberIds().add(userdetails.getUserId());
        
        var save = communityRepository.save(communityMapper.mapDtoToCommunity(communityPackage,Collections.singleton(userdetails)));
        communityPackage.setCommunityId(save.getCommunityId());

        //First time saving a new community.
        if (save.getOwnerUsers() == null){
            communityPackage.setOwnerUsers(Collections.singleton(userdetails));
        }
        return communityPackage;
    }

    @Transactional
    public CommunityPackage update(CommunityPackage communityPackage){
        communityRepository.save(communityMapper.mapDtoToCommunity(communityPackage));
        return communityPackage;
    }

    @Transactional
    public List<CommunityPackage> getAllCommunities() {
        return communityRepository.findAll().stream().map(communityMapper:: mapCommunityPackage)
        .collect(Collectors.toList());
    }

    @Transactional
    public CommunityPackage getCommunityPackageById(Long id) {
        Community com = communityRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("ID NOT FOUND"));
        return communityMapper.mapCommunityPackage(com);
    }

    @Transactional
    public CommunityPackage getCommunityPackagebyName(String name){
        Community com = communityRepository.findByName(name)
        .orElseThrow(() -> new RuntimeException(name + " NOT FOUND!!"));

        return communityMapper.mapCommunityPackage(com);
    }

    //Intended for method-use
    @Transactional
    public CommunityPackage getCommunityByName(String name){
        Community com = communityRepository.findByName(name)
        .orElseThrow(() -> new RuntimeException(name + " NOT FOUND!!"));

        return communityMapper.mapCommunityPackage(com);
    }

    //Intended for method-use
    @Transactional
    public Community getCommunityById(Long id){
        Community com = communityRepository.findByCommunityId(id)
        .orElseThrow(() -> new RuntimeException(id.toString() + "NOT FOUND"));

        return com;
    }

    @Transactional
    public List<UserPackage> getOwnerUsersByCommunityId(Long id){
        Community com = getCommunityById(id);

        List<UserPackage> users = new ArrayList<>(com.getOwnerUsers()).stream().map(userMapper::mapToPackage)
        .collect(toList());
        return users;
    }

    @Transactional
    public List<CommunityPackage> getAllCommunitiesByOwnerId(Long id) {
        User user = studyholeService.getUserByUserId(id);

        return communityRepository.findByOwnerUsers(user).stream().map(communityMapper::mapCommunityPackage)
        .collect(Collectors.toList());
    }

    @Transactional
    public CommunityPackage getCommunityByPostId(Long id){
        Post post = studyholeService.getPostById(id);

        return communityMapper.mapCommunityPackage(post.getOwnerCommunity());
    }
    @Transactional
    public List<UserPackage> getAllMembersByCommunityId(Long id){
        Community com = getCommunityById(id);

        List<User> users = studyholeService.getAllUsersByIds(com.getMemberIds());

        List<UserPackage> members = users.stream()
        .map(user -> userMapper.mapToPackage(user))
        .collect(Collectors.toList());

        return members;
    }
    @Transactional
    public List<UserPackage> getAllAppliedMembersByCommunityId(Long id){
        Community com = getCommunityById(id);

        List<User> users = studyholeService.getAllUsersByIds(com.getAppliedMemberIds());

        List<UserPackage> members = users.stream()
        .map(user -> userMapper.mapToPackage(user))
        .collect(Collectors.toList());

        return members;
    }
    @Transactional
    public List<CommunityPackage> getAllCommunitiesByUsername(String username){
        User user = studyholeService.getUserbyUsername(username);

        return communityRepository.findAllByMemberIdsContaining(user.getUserId()).stream().map(communityMapper::mapCommunityPackage)
        .collect(Collectors.toList());
    }

    @Transactional
    public String uploadImageToCommunity(Long communityId,MultipartFile  file) throws IOException {
        Community com = getCommunityById(communityId);
        byte[] imageData = ImageUtils.compressImage(file.getBytes());
        Image image = Image.builder().imageData(imageData)
        .build();
        Image im = imageRepository.save(image);

        com.setCommunityImage(im);
        communityRepository.save(com);

        return " 'uploaded'";
    }

    @Transactional
    public ImagePackage getCommunityImage(Long communityId) throws IOException, DataFormatException{
        Community com = getCommunityById(communityId);
        ImagePackage imPack =  imageMapper.mapToPackage(com.getCommunityImage());
        imPack.setImageData(ImageUtils.decompressImage(imPack.getImageData()));

        return imPack;
    }

    @Transactional
    public void AcceptUserToCommunity(Long communityId, Long userId){
        User user = studyholeService.getUserByUserId(userId);
        CommunityPackage com = communityMapper.mapCommunityPackage(studyholeService.getCommunityById(communityId));
        com.getAppliedMemberIds().remove(user.getUserId());
        update(com);
        studyholeService.subscribeUserToCommunity(user, com);
    }
}
