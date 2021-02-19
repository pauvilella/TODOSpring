package org.udg.pds.springtodo.service;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.GroupRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserService userService;

    public GroupRepository crud() { return groupRepository; }

    @Transactional
    public IdObject addGroup(String name, Long userId, String description) {
        try {
            User user = userService.getUser(userId);

            Group group = new Group(name, description);

            group.setOwner(user);

            user.addGroup(group);

            groupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    public Collection<Group> getGroups(Long id) {
        return userService.getUser(id).getGroups();
    }

    public Collection<User> getMembers(Long userId,Long groupId) {
        Group g = getGroup(userId, groupId);
        return g.getMembers();
    }

    public Group getGroup(Long userId, Long id) {
        Optional<Group> g = groupRepository.findById(id);
        if (!g.isPresent()) throw new ServiceException("Group does not exists");
        if (g.get().getOwner().getId() != userId)
            throw new ServiceException("User does not own this group");
        return g.get();
    }

    @Transactional
    public void addMemberToGroup(Long userId, Long groupId, Long memberId) {
        Group g = this.getGroup(userId, groupId);

        try {
            User user = userService.getUser(memberId);
            g.addMember(user);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
}
