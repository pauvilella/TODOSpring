package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.serializer.JsonDateDeserializer;
import org.udg.pds.springtodo.service.GroupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@RequestMapping(path="/groups")
@RestController
public class GroupController extends BaseController {

    @Autowired
    GroupService groupService;

    @PostMapping(consumes = "application/json")
    public IdObject addGroup(HttpSession session, @Valid @RequestBody GroupController.R_Group group) {

        Long userId = getLoggedUser(session);

        return groupService.addGroup(group.name, userId, group.description);
    }

    @PostMapping(path="/{gid}/members/{uid}")
    public String addMember(HttpSession session,
                          @PathVariable("gid") Long groupId, @PathVariable("uid") Long memberId) {

        Long userId = getLoggedUser(session);
        groupService.addMemberToGroup(userId, groupId, memberId);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/{gid}/members")
    @JsonView(Views.Complete.class)
    public Collection<User> listGroupMembers(HttpSession session, @PathVariable("gid") Long groupId) {
        Long userId = getLoggedUser(session);
        return groupService.getMembers(userId, groupId);
    }

    static class R_Group {

        @NotNull
        public String name;

        @NotNull
        public String description;
    }
}
