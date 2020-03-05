package com.mammoth.soft.huarongdao.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
import android.content.res.Resources;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.ui.game.GameFragment;
import com.mammoth.soft.huarongdao.utils.Chess;
import com.mammoth.soft.huarongdao.utils.CommonFuncsUtils;
import com.mammoth.soft.huarongdao.utils.GameHRD;

public class HrdView extends View implements GestureDetector.OnGestureListener {

    private static String TAG = "HrdView";
    private GameFragment.OnStepListener onStepListener = null;
    private Context context = null;

    private int measureHeight = 0;
    private int measureWidth = 0;

    private boolean bCaocao = false;
    private boolean bGuanyuH = false;
    private boolean bGuanyuV = false;
    private boolean bZhangfeiH = false;
    private boolean bZhangfeiV = false;
    private boolean bZhaoyunH = false;
    private boolean bZhaoyunV = false;
    private boolean bMachaoH = false;
    private boolean bMachaoV = false;
    private boolean bHuangzhongH = false;
    private boolean bHuangzhongV = false;
    private boolean bSoldier1 = false;
    private boolean bSoldier2 = false;
    private boolean bSoldier3 = false;
    private boolean bSoldier4 = false;

    private static int time = 0;
    private static int step = 0;
    private static int vol = 4;//列数
    private static int row = 5;//行数
    public static final int MOVE_TO_LEFT = 0;
    public static final int MOVE_TO_RIGHT = MOVE_TO_LEFT + 1;
    public static final int MOVE_TO_UP = MOVE_TO_RIGHT + 1;
    public static final int MOVE_TO_DOWN = MOVE_TO_UP + 1;

    private static final int FLING_MIN_DISTANCE = 20;
    private static final int FLING_MIN_VELOCITY = 50;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    int currPos_x = 0;
    int currPos_y = 0;
    private int derection = MOVE_TO_LEFT;
    private int level = 0;// 游戏选关，第一关：横刀立马
    private int[][] chessboard = new int[vol][row];// 四列五行的棋盘
    private Chess[] chessArray = null;
    private GestureDetector mGestureDetector;
    private int orig_x = 0;
    private int orig_y = 0;
    private int unit = 120;//一个单元格的像素尺寸
    private int frame= 5;//一个单元格的像素尺寸

    private int piontPos = 0;//用户点中的位置
    private GameFragment.OnSoundListener onSoundListener;
    private boolean bMusic = false;
    private boolean bSound = true;
    private boolean bWin = false;
    public HrdView(Context context, int level, GameFragment.OnStepListener onStepListener, GameFragment.OnSoundListener onSoundListener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.level = level;
        this.onStepListener = onStepListener;
        this.onSoundListener = onSoundListener;
        bMusic = CommonFuncsUtils.getMusicSet(context, false);
        bSound = CommonFuncsUtils.getSoundSet(context, false);
        mGestureDetector = new GestureDetector(context, (OnGestureListener) this);
        GameHRD gameHRD = CommonFuncsUtils.listGameHRD.get(level);
        step = gameHRD.gethStep();
        createChess(level,false);
        setFocusable(true);
    }
    public void reset() {
        step = 0;
        bCaocao = false;
        bGuanyuH = false;
        bGuanyuV = false;
        bZhangfeiH = false;
        bZhangfeiV = false;
        bZhaoyunH = false;
        bZhaoyunV = false;
        bMachaoH = false;
        bMachaoV = false;
        bHuangzhongH = false;
        bHuangzhongV = false;
        bSoldier1 = false;
        bSoldier2 = false;
        bSoldier3 = false;
        bSoldier4 = false;
        createChess(level, true);
        setFocusable(true);
        this.invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        drawChess(canvas);
        bWin = checkWin();
        if(onStepListener != null){
            onStepListener.onEvent(step,bWin);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        measureHeight = measure(heightMeasureSpec);
        measureWidth = measure(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measure(int measureSpec) {
        // TODO Auto-generated method stub
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.UNSPECIFIED){
            result = 400;
        } else{
            result = specSize;
        }
        return result;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        if(bWin){
            return false;
        }
        x1 = e1.getX();
        y1 = e1.getY();
        x2 = e2.getX();
        y2 = e2.getY();
        Log.e(TAG, "AA:A" + "x1="+x1+" / y1="+y1+" / x2="+x2+" / y2="+y2);
        if ((e1.getX() - e2.getX() > FLING_MIN_DISTANCE)
                && Math.abs(velocityX) > FLING_MIN_VELOCITY
                && (Math.abs(e2.getX()-e1.getX())/Math.abs(e2.getY()-e1.getY()))>2) {
            // Fling left
            derection =  MOVE_TO_LEFT;
        } else if ((e2.getX() - e1.getX() > FLING_MIN_DISTANCE)
                && Math.abs(velocityX) > FLING_MIN_VELOCITY
                && (Math.abs(e2.getX()-e1.getX())/Math.abs(e2.getY()-e1.getY()))>2) {
            // Fling right
            derection = MOVE_TO_RIGHT;
        } else if ((e1.getY() - e2.getY() > FLING_MIN_DISTANCE)
                && Math.abs(velocityY) > FLING_MIN_VELOCITY
                && (Math.abs(e2.getY()-e1.getY())/Math.abs(e2.getX()-e1.getX()))>2) {
            // Fling up
            derection = MOVE_TO_UP;
        } else if ((e2.getY() - e1.getY() > FLING_MIN_DISTANCE)
                && Math.abs(velocityY) > FLING_MIN_VELOCITY
                && (Math.abs(e2.getY()-e1.getY())/Math.abs(e2.getX()-e1.getX()))>2) {
            // Fling up
            derection = MOVE_TO_DOWN;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
//		return super.onTouchEvent(event);
        if (mGestureDetector.onTouchEvent(event))
            return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if(bSound){
                    onSoundListener.onEvent(1);
                }
                endGesture();
                break;
        }
        return false;
    }

    private void endGesture() {
        // TODO Auto-generated method stub
        moveOver();
    }

    private void moveOver() {
        // TODO Auto-generated method stub
        Log.d(TAG, "end move");
        if(!inFrame()){
            Log.d(TAG, "not in frame");
            return;
        }
        piontPos  = getPosition();
        if(derection ==  MOVE_TO_LEFT){
            Log.e(TAG, "AA:A" + "derection ==  MOVE_TO_LEFT");
            moveLeft();
        } else if(derection ==  MOVE_TO_RIGHT){
            Log.e(TAG, "AA:A" + "derection ==  MOVE_TO_RIGHT");
            moveRight();
        } else if(derection ==  MOVE_TO_UP){
            Log.e(TAG, "AA:A" + "derection ==  MOVE_TO_UP");
            moveUp();
        } else if(derection ==  MOVE_TO_DOWN){
            Log.e(TAG, "AA:A" + "derection ==  MOVE_TO_DOWN");
            moveDown();
        }
    }

    private void moveLeft() {
        // TODO Auto-generated method stub
        int i = 0;
        boolean isFound = false;
        if(currPos_x == 0){
            return;
        } else{
            switch(piontPos){
                case Chess.CHESS_TYPE_CAOCAO:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_X <= 0){
                            return;
                        }
                        if(chessboard[temp_X-1][temp_Y] == 0
                                && chessboard[temp_X-1][temp_Y+1] == 0){
                            chessboard[temp_X+1][temp_Y] = 0;
                            chessboard[temp_X+1][temp_Y+1] = 0;
                            chessboard[temp_X-1][temp_Y] = piontPos;
                            chessboard[temp_X-1][temp_Y+1] = piontPos;
                            chessArray[i].setXPos(temp_X-1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_GUANYU_H:
                case Chess.CHESS_TYPE_ZHANGFEI_H:
                case Chess.CHESS_TYPE_ZHAOYUN_H:
                case Chess.CHESS_TYPE_MACHAO_H:
                case Chess.CHESS_TYPE_HUANGZHONG_H:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_X <= 0){
                            return;
                        }
                        if(chessboard[temp_X-1][temp_Y] == 0){
                            chessboard[temp_X+1][temp_Y] = 0;
                            chessboard[temp_X-1][temp_Y] = piontPos;
                            chessArray[i].setXPos(temp_X-1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_ZHANGFEI_V:
                case Chess.CHESS_TYPE_ZHAOYUN_V:
                case Chess.CHESS_TYPE_MACHAO_V:
                case Chess.CHESS_TYPE_HUANGZHONG_V:
                case Chess.CHESS_TYPE_GUANYU_V:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_X <= 0){
                            return;
                        }
                        if(chessboard[temp_X-1][temp_Y] == 0
                                && chessboard[temp_X-1][temp_Y+1] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X][temp_Y+1] = 0;
                            chessboard[temp_X-1][temp_Y] = piontPos;
                            chessboard[temp_X-1][temp_Y+1] = piontPos;
                            chessArray[i].setXPos(temp_X-1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_SOLDIER1:
                case Chess.CHESS_TYPE_SOLDIER2:
                case Chess.CHESS_TYPE_SOLDIER3:
                case Chess.CHESS_TYPE_SOLDIER4:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_X <= 0){
                            return;
                        }
                        if(chessboard[temp_X-1][temp_Y] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X-1][temp_Y] = piontPos;
                            chessArray[i].setXPos(temp_X-1);
                            step ++;
                        }
                    }
                    break;
                default:
                    break;
            }
            postInvalidate();
        }
    }

    private void moveRight() {
        // TODO Auto-generated method stub
        int i = 0;
        boolean isFound = false;
        if(currPos_x == 3){
            return;
        } else{
            switch(piontPos){
                case Chess.CHESS_TYPE_CAOCAO:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_X >= 2){
                            return;
                        }
                        if(chessboard[temp_X+2][temp_Y] == 0
                                && chessboard[temp_X+2][temp_Y+1] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X][temp_Y+1] = 0;
                            chessboard[temp_X+2][temp_Y] = piontPos;
                            chessboard[temp_X+2][temp_Y+1] = piontPos;
                            chessArray[i].setXPos(temp_X+1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_GUANYU_H:
                case Chess.CHESS_TYPE_ZHANGFEI_H:
                case Chess.CHESS_TYPE_ZHAOYUN_H:
                case Chess.CHESS_TYPE_MACHAO_H:
                case Chess.CHESS_TYPE_HUANGZHONG_H:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_X >= 2){
                            return;
                        }
                        if(chessboard[temp_X+2][temp_Y] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X+2][temp_Y] = piontPos;
                            chessArray[i].setXPos(temp_X+1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_ZHANGFEI_V:
                case Chess.CHESS_TYPE_ZHAOYUN_V:
                case Chess.CHESS_TYPE_MACHAO_V:
                case Chess.CHESS_TYPE_HUANGZHONG_V:
                case Chess.CHESS_TYPE_GUANYU_V:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_X >= 3){
                            return;
                        }
                        if(chessboard[temp_X+1][temp_Y] == 0
                                && chessboard[temp_X+1][temp_Y+1] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X][temp_Y+1] = 0;
                            chessboard[temp_X+1][temp_Y] = piontPos;
                            chessboard[temp_X+1][temp_Y+1] = piontPos;
                            chessArray[i].setXPos(temp_X+1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_SOLDIER1:
                case Chess.CHESS_TYPE_SOLDIER2:
                case Chess.CHESS_TYPE_SOLDIER3:
                case Chess.CHESS_TYPE_SOLDIER4:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_X >= 3){
                            return;
                        }
                        if(chessboard[temp_X+1][temp_Y] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X+1][temp_Y] = piontPos;
                            chessArray[i].setXPos(temp_X+1);
                            step ++;
                        }
                    }
                    break;
                default:
                    break;
            }
            postInvalidate();
        }
    }

    private void moveUp() {
        // TODO Auto-generated method stub
        int i = 0;
        boolean isFound = false;
        if(currPos_y == 0){
            return;
        } else{
            switch(piontPos){
                case Chess.CHESS_TYPE_CAOCAO:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_Y <= 0){
                            return;
                        }
                        if(chessboard[temp_X][temp_Y-1] == 0
                                && chessboard[temp_X+1][temp_Y-1] == 0){
                            chessboard[temp_X][temp_Y+1] = 0;
                            chessboard[temp_X+1][temp_Y+1] = 0;
                            chessboard[temp_X][temp_Y-1] = piontPos;
                            chessboard[temp_X+1][temp_Y-1] = piontPos;
                            chessArray[i].setYPos(temp_Y-1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_GUANYU_H:
                case Chess.CHESS_TYPE_ZHANGFEI_H:
                case Chess.CHESS_TYPE_ZHAOYUN_H:
                case Chess.CHESS_TYPE_MACHAO_H:
                case Chess.CHESS_TYPE_HUANGZHONG_H:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_Y <= 0){
                            return;
                        }
                        if(chessboard[temp_X][temp_Y-1] == 0
                                && chessboard[temp_X+1][temp_Y-1] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X+1][temp_Y] = 0;
                            chessboard[temp_X][temp_Y-1] = piontPos;
                            chessboard[temp_X+1][temp_Y-1] = piontPos;
                            chessArray[i].setYPos(temp_Y-1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_ZHANGFEI_V:
                case Chess.CHESS_TYPE_ZHAOYUN_V:
                case Chess.CHESS_TYPE_MACHAO_V:
                case Chess.CHESS_TYPE_HUANGZHONG_V:
                case Chess.CHESS_TYPE_GUANYU_V:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_Y <= 0){
                            return;
                        }
                        if(chessboard[temp_X][temp_Y-1] == 0){
                            chessboard[temp_X][temp_Y+1] = 0;
                            chessboard[temp_X][temp_Y-1] = piontPos;
                            chessArray[i].setYPos(temp_Y-1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_SOLDIER1:
                case Chess.CHESS_TYPE_SOLDIER2:
                case Chess.CHESS_TYPE_SOLDIER3:
                case Chess.CHESS_TYPE_SOLDIER4:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_Y <= 0){
                            return;
                        }
                        if(chessboard[temp_X][temp_Y-1] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X][temp_Y-1] = piontPos;
                            chessArray[i].setYPos(temp_Y-1);
                            step ++;
                        }
                    }
                    break;
                default:
                    break;
            }
            postInvalidate();
        }
    }

    private void moveDown() {
        // TODO Auto-generated method stub
        int i = 0;
        boolean isFoun = false;
        if(currPos_y == 4){
            return;
        } else{
            switch(piontPos){
                case Chess.CHESS_TYPE_CAOCAO:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFoun = true;
                            break;
                        }
                    }
                    if(isFoun){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_Y >= 3){
                            return;
                        }
                        if(chessboard[temp_X][temp_Y+2] == 0
                                && chessboard[temp_X+1][temp_Y+2] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X+1][temp_Y] = 0;
                            chessboard[temp_X][temp_Y+2] = piontPos;
                            chessboard[temp_X+1][temp_Y+2] = piontPos;
                            chessArray[i].setYPos(temp_Y+1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_GUANYU_H:
                case Chess.CHESS_TYPE_ZHANGFEI_H:
                case Chess.CHESS_TYPE_ZHAOYUN_H:
                case Chess.CHESS_TYPE_MACHAO_H:
                case Chess.CHESS_TYPE_HUANGZHONG_H:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFoun = true;
                            break;
                        }
                    }
                    if(isFoun){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_Y >= 4){
                            return;
                        }
                        if(chessboard[temp_X][temp_Y+1] == 0
                                && chessboard[temp_X+1][temp_Y+1] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X+1][temp_Y] = 0;
                            chessboard[temp_X][temp_Y+1] = piontPos;
                            chessboard[temp_X+1][temp_Y+1] = piontPos;
                            chessArray[i].setYPos(temp_Y+1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_ZHANGFEI_V:
                case Chess.CHESS_TYPE_ZHAOYUN_V:
                case Chess.CHESS_TYPE_MACHAO_V:
                case Chess.CHESS_TYPE_HUANGZHONG_V:
                case Chess.CHESS_TYPE_GUANYU_V:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFoun = true;
                            break;
                        }
                    }
                    if(isFoun){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_Y >= 3){
                            return;
                        }
                        if(chessboard[temp_X][temp_Y+2] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X][temp_Y+2] = piontPos;
                            chessArray[i].setYPos(temp_Y+1);
                            step ++;
                        }
                    }
                    break;
                case Chess.CHESS_TYPE_SOLDIER1:
                case Chess.CHESS_TYPE_SOLDIER2:
                case Chess.CHESS_TYPE_SOLDIER3:
                case Chess.CHESS_TYPE_SOLDIER4:
                    for(i=0;i<chessArray.length;i++){
                        if(chessArray[i].getType() == piontPos){
                            isFoun = true;
                            break;
                        }
                    }
                    if(isFoun){
                        int temp_X = chessArray[i].getXPos();
                        int temp_Y = chessArray[i].getYPos();
                        if(temp_Y >= 4){
                            return;
                        }
                        if(chessboard[temp_X][temp_Y+1] == 0){
                            chessboard[temp_X][temp_Y] = 0;
                            chessboard[temp_X][temp_Y+1] = piontPos;
                            chessArray[i].setYPos(temp_Y+1);
                            step ++;
                        }
                    }
                    break;
                default:
                    break;
            }
            postInvalidate();
        }
    }

    private int getPosition() {
        // TODO Auto-generated method stub
        currPos_x = (int) ((x1-orig_x)/unit);
        currPos_y = (int) ((y1-orig_y)/unit);
        return chessboard[currPos_x][currPos_y];
    }

    private boolean inFrame() {
        // TODO Auto-generated method stub
        if(x1 < orig_x || x1 > orig_x+unit*4){
            return false;
        } else if(y1 < orig_y || y1 > orig_y+unit*5){
            return false;
        }
        return true;
    }

    private boolean createChess(int Level, boolean bReset) {
        Resources res = getResources();
        int i = 0;
        int j = 0;
        int k = 0;
        chessArray = new Chess[10];
        for(k =0;k<chessArray.length;k++){
            chessArray[k] = null;
        }
        initChessBoard();
        if(bReset){
            chessboard = CommonFuncsUtils.listGameHRD.get(level).getIntegerOrigMap();
        } else {
            chessboard = CommonFuncsUtils.listGameHRD.get(level).getIntegerMap();
        }
        k = 0;
        for(i=0;i<chessboard.length;i++){
            for(j=0;j<chessboard[i].length;j++){
                switch(chessboard[i][j]){
                    case Chess.CHESS_TYPE_CAOCAO:
                        if(!bCaocao){
                            bCaocao = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_CAOCAO,
                                            Chess.CHESS_NAME_CAOCAO, BitmapFactory.decodeResource(res,
                                            R.drawable.caocao), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_GUANYU_H:
                        if(!bGuanyuH){
                            bGuanyuH = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_GUANYU_H,
                                            Chess.CHESS_NAME_GUANYU, BitmapFactory.decodeResource(res,
                                            R.drawable.general_gy), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_GUANYU_V:
                        if(!bGuanyuV){
                            bGuanyuV = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_GUANYU_V,
                                            Chess.CHESS_NAME_GUANYU, BitmapFactory.decodeResource(res,
                                            R.drawable.guanyu), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_ZHANGFEI_H:
                        if(!bZhangfeiH){
                            bZhangfeiH = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_ZHANGFEI_H,
                                            Chess.CHESS_NAME_ZHANGFEI, BitmapFactory.decodeResource(res,
                                            R.drawable.zhangfei), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_ZHANGFEI_V:
                        if(!bZhangfeiV){
                            bZhangfeiV = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_ZHANGFEI_V,
                                            Chess.CHESS_NAME_ZHANGFEI, BitmapFactory.decodeResource(res,
                                            R.drawable.general_zf), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_ZHAOYUN_H:
                        if(!bZhaoyunH){
                            bZhaoyunH = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_ZHAOYUN_H,
                                            Chess.CHESS_NAME_ZHAOYUN, BitmapFactory.decodeResource(res,
                                            R.drawable.zhaoyun), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_ZHAOYUN_V:
                        if(!bZhaoyunV){
                            bZhaoyunV = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_ZHAOYUN_V,
                                            Chess.CHESS_NAME_ZHAOYUN, BitmapFactory.decodeResource(res,
                                            R.drawable.general_zy), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_MACHAO_H:
                        if(!bMachaoH){
                            bMachaoH = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_MACHAO_H,
                                            Chess.CHESS_NAME_MACHAO, BitmapFactory.decodeResource(res,
                                            R.drawable.machao), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_MACHAO_V:
                        if(!bMachaoV){
                            bMachaoV = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_MACHAO_V,
                                            Chess.CHESS_NAME_ZHAOYUN, BitmapFactory.decodeResource(res,
                                            R.drawable.general_mc), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_HUANGZHONG_H:
                        if(!bHuangzhongH){
                            bHuangzhongH = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_HUANGZHONG_H,
                                            Chess.CHESS_NAME_HUANGZHONG, BitmapFactory.decodeResource(res,
                                            R.drawable.huangzhong), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_HUANGZHONG_V:
                        if(!bHuangzhongV){
                            bHuangzhongV = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_HUANGZHONG_V,
                                            Chess.CHESS_NAME_HUANGZHONG, BitmapFactory.decodeResource(res,
                                            R.drawable.general_hz), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_SOLDIER1:
                        if(!bSoldier1){
                            bSoldier1 = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_SOLDIER1,
                                            Chess.CHESS_NAME_SOLDIER1, BitmapFactory.decodeResource(res,
                                            R.drawable.soldier1), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_SOLDIER2:
                        if(!bSoldier2){
                            bSoldier2 = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_SOLDIER2,
                                            Chess.CHESS_NAME_SOLDIER2, BitmapFactory.decodeResource(res,
                                            R.drawable.soldier1), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_SOLDIER3:
                        if(!bSoldier3){
                            bSoldier3 = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_SOLDIER3,
                                            Chess.CHESS_NAME_SOLDIER3, BitmapFactory.decodeResource(res,
                                            R.drawable.soldier1), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                    case Chess.CHESS_TYPE_SOLDIER4:
                        if(!bSoldier4){
                            bSoldier4 = true;
                            for(k =0;k<chessArray.length;k++){
                                if(chessArray[k] == null){
                                    chessArray[k] = new Chess(Chess.CHESS_TYPE_SOLDIER4,
                                            Chess.CHESS_NAME_SOLDIER4, BitmapFactory.decodeResource(res,
                                            R.drawable.soldier1), i, j);
                                    k = 0;
                                    break;
                                }
                            }

                        }
                        break;
                }
            }
        }
        return true;
    }

    public int[][] getChessBoard(){
        return chessboard;
    }
    private void initChessBoard() {
        for(int i=0;i<chessboard.length;i++){
            for(int j=0;j<chessboard[0].length;j++){
                chessboard[i][j] = 0;
            }
        }
    }


    private void drawChess(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        int width = measureWidth;
        int height = measureHeight;
        unit = Math.min((width-frame*2)/4 ,(height-frame*2-20)/5);
        orig_x = width/2 - unit*2;
        orig_y = (height-20)/2 - unit*2 - unit/2;
        for(int i=0;i<chessArray.length;i++)
        {
            Bitmap bitmap = chessArray[i].getBitmap();
            RectF dst = null;
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            int type = chessArray[i].getType();
            if(type == Chess.CHESS_TYPE_CAOCAO){
                dst = new RectF(orig_x+chessArray[i].getXPos()*unit, orig_y+chessArray[i].getYPos()*unit, orig_x+chessArray[i].getXPos()*unit+unit*2, orig_y+chessArray[i].getYPos()*unit+unit*2);
            } else if(type == Chess.CHESS_TYPE_SOLDIER1 || type == Chess.CHESS_TYPE_SOLDIER2 || type == Chess.CHESS_TYPE_SOLDIER3 || type == Chess.CHESS_TYPE_SOLDIER4){
                dst = new RectF(orig_x+chessArray[i].getXPos()*unit, orig_y+chessArray[i].getYPos()*unit, orig_x+chessArray[i].getXPos()*unit+unit, orig_y+chessArray[i].getYPos()*unit+unit);
            } else if(type == Chess.CHESS_TYPE_GUANYU_H || type == Chess.CHESS_TYPE_ZHANGFEI_H ||type == Chess.CHESS_TYPE_ZHAOYUN_H || type == Chess.CHESS_TYPE_MACHAO_H || type == Chess.CHESS_TYPE_HUANGZHONG_H){
                dst = new RectF(orig_x+chessArray[i].getXPos()*unit, orig_y+chessArray[i].getYPos()*unit, orig_x+chessArray[i].getXPos()*unit+unit*2, orig_y+chessArray[i].getYPos()*unit+unit);
            } else if(type == Chess.CHESS_TYPE_GUANYU_V || type == Chess.CHESS_TYPE_ZHANGFEI_V || type == Chess.CHESS_TYPE_ZHAOYUN_V || type == Chess.CHESS_TYPE_MACHAO_V || type == Chess.CHESS_TYPE_HUANGZHONG_V){
                dst = new RectF(orig_x+chessArray[i].getXPos()*unit, orig_y+chessArray[i].getYPos()*unit, orig_x+chessArray[i].getXPos()*unit+unit, orig_y+chessArray[i].getYPos()*unit+unit*2);
            }
            canvas.drawBitmap(bitmap, src, dst, paint);//(chessArray[i].getBitmap(), orig_x+chessArray[i].getXPos()*unit,orig_y+chessArray[i].getYPos()*unit, paint);
        }

        canvas.drawRect(orig_x-frame, orig_y+unit*5, orig_x+unit, orig_y+unit*5+frame, paint);
        canvas.drawRect(orig_x+unit*3, orig_y+unit*5, orig_x+unit*4+frame, orig_y+unit*5+frame, paint);
        canvas.drawRect(orig_x-frame, orig_y-frame, orig_x, orig_y+unit*5+frame, paint);
        canvas.drawRect(orig_x-frame, orig_y-frame, orig_x+unit*4+frame, orig_y, paint);
        canvas.drawRect(orig_x+unit*4, orig_y-frame, orig_x+unit*4+frame, orig_y+unit*5+frame, paint);
    }

    private boolean checkWin() {
        // TODO Auto-generated method stub
        boolean isFound = false;

        for(int i=0;i<chessArray.length;i++){
            if(chessArray[i].getType() == Chess.CHESS_TYPE_CAOCAO){
                if(chessArray[i].getXPos() == 1
                        && chessArray[i].getYPos() == 3){
                    isFound = true;
                    if(bSound){
                        onSoundListener.onEvent(0);
                    }
                    break;
                }

            }
        }
//		if(isFound){
//			String a = getResources().getString(R.string.win_msg);
//			String b = String.format(a, step);
//			AlertDialog.Builder builder = new AlertDialog.Builder(
//					context);
//			builder.setTitle(R.string.win_title)
//			.setMessage(b)
//			.setPositiveButton(R.string.bt_ok,
//					new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog,
//						int which) {
//					step = 0;
//					((GameActivity)context).finish();
//				}
//			}).show();
//		}
        return isFound;
    }

    public int getCurrentStep() {
        return step;
    }
}