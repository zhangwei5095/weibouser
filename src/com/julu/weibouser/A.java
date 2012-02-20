package com.julu.weibouser;

import com.julu.weibouser.integration.Integration;
import com.julu.weibouser.integration.SinaWeibo;
import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.template.ListTemplate;
import org.msgpack.template.Template;
import org.msgpack.unpacker.Unpacker;
import weibo4j.Friendships;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.String;
import java.util.Arrays;
import java.util.List;

public class A {
    
    public static void main(String[] args) {
        Weibo weibo = new Weibo();
        weibo.setToken("2.00tyhuHC09EMdOe80ef5d168vfOZbD");
        String uid =  "1709498127";
        Users um = new Users();
        try {
            User user = um.showUserById(uid);
            Log.logInfo(user.toString());

            user.getFollowersCount();
            
            com.julu.weibouser.model.User modelUser = com.julu.weibouser.model.User.compose(user, Integration.getSinaWeiboType());

            MessagePack msgpack = new MessagePack();
            msgpack.register(com.julu.weibouser.model.User.class);

            byte[] bytes = msgpack.write(modelUser);
            System.out.println(bytes.length);

            MessagePack msgpack1 = new MessagePack();
            msgpack1.register(com.julu.weibouser.model.User.class);
            com.julu.weibouser.model.User testUser = msgpack1.read(bytes, com.julu.weibouser.model.User.class);
            System.out.println(testUser.getOriginalSourceUid());

            MessagePack msgpack2 = new MessagePack();
            Template<com.julu.weibouser.model.User> elementTemplate = msgpack.lookup(com.julu.weibouser.model.User.class);
            Template<List<com.julu.weibouser.model.User>> tmpl = new ListTemplate<com.julu.weibouser.model.User>(elementTemplate);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Packer packer = msgpack.createPacker(out);
            tmpl.write(packer, Arrays.asList(testUser));
            bytes = out.toByteArray();

            Unpacker unpacker = msgpack.createUnpacker(new ByteArrayInputStream(bytes));
            unpacker.resetReadByteCount();
            List<com.julu.weibouser.model.User> ret = tmpl.read(unpacker, null);
            
            System.out.println(ret.size());
            System.out.println(ret.get(0).getOriginalSourceUid());

        } catch (WeiboException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            User user = um.showUserByScreenName("孙楠");
            Log.logInfo(user.toString());

            user.getFollowersCount();
        } catch (WeiboException e) {
            e.printStackTrace();
        }*/

        Friendships friendships = new Friendships();
        try {
            UserWapper userWapper = friendships.getFollowersById("1709498127");
            List<User> users = userWapper.getUsers();
            
            for(User user:users) {
                Log.logInfo(user.toString());
            }
        } catch (WeiboException e) {
            e.printStackTrace();
        }

        /*try {
            UserWapper userWapper = friendships.getFollowersById("1949215451", 2, 1);
            List<User> users = userWapper.getUsers();

            for(User user:users) {
                Log.logInfo(user.toString());
            }
        } catch (WeiboException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
    }
    
}