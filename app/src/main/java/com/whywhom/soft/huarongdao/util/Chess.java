package com.whywhom.soft.huarongdao.util;

import android.graphics.Bitmap;

public class Chess {
	public static final int CHESS_TYPE_CAOCAO = 1;
	public static final int CHESS_TYPE_GUANYU_H = 2;
	public static final int CHESS_TYPE_GUANYU_V = 3;
	public static final int CHESS_TYPE_ZHANGFEI_H = 4;
	public static final int CHESS_TYPE_ZHANGFEI_V = 5;
	public static final int CHESS_TYPE_ZHAOYUN_H = 6;
	public static final int CHESS_TYPE_ZHAOYUN_V = 7;
	public static final int CHESS_TYPE_MACHAO_H = 8;
	public static final int CHESS_TYPE_MACHAO_V = 9;
	public static final int CHESS_TYPE_HUANGZHONG_H = 0xA;
	public static final int CHESS_TYPE_HUANGZHONG_V = 0xB;
	public static final int CHESS_TYPE_SOLDIER1 = 0xC;
	public static final int CHESS_TYPE_SOLDIER2 = 0xD;
	public static final int CHESS_TYPE_SOLDIER3 = 0xE;
	public static final int CHESS_TYPE_SOLDIER4 = 0xF;
	
	public static String CHESS_NAME_CAOCAO = "caocao";
	public static String CHESS_NAME_GUANYU = "guanyu";
	public static String CHESS_NAME_ZHANGFEI = "zhangfei";
	public static String CHESS_NAME_ZHAOYUN = "zhaoyun";
	public static String CHESS_NAME_MACHAO = "machao";
	public static String CHESS_NAME_HUANGZHONG = "huangzhong";
	public static String CHESS_NAME_SOLDIER1 = "soldier1";
	public static String CHESS_NAME_SOLDIER2 = "soldier2";
	public static String CHESS_NAME_SOLDIER3 = "soldier3";
	public static String CHESS_NAME_SOLDIER4 = "soldier4";
	
	private int type = CHESS_TYPE_CAOCAO;
	private String name = CHESS_NAME_CAOCAO;
	private int pos_x = 0;//左上角横坐标，相对于棋盘左上角
	private int pos_y = 0;//左上角纵坐标，相对于棋盘左上角
	private Bitmap bmp =null;
	public Chess(int type, String name, Bitmap bmp,int xpos, int ypos){
		this.type = type;
		this.name = name;
		this.bmp = bmp;
		this.pos_x = xpos;
		this.pos_y = ypos;
	}
	public int getType(){
		return type;
	}
	public String getName(){
		return name;
	}
	public Bitmap getBitmap(){
		return bmp;
	}
	public int getXPos(){
		return pos_x;
	}
	public void setXPos(int pos){
		pos_x = pos;
	}
	public int getYPos(){
		return pos_y;
	}
	public void setYPos(int pos){
		pos_y = pos;
	}
}
