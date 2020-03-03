package com.mammoth.soft.huarongdao.user;

public interface IUser {
    /**
     * Returns the User's id
     *
     * @return the User's id
     * */
    String getId();

    /**
     * Returns the User's name
     *
     * @return the User's name
     * */
    String getName();

    /**
     * Returns the User's avatar image url
     *
     * @return the User's avatar image url
     * */
    String getAvatar();
}
