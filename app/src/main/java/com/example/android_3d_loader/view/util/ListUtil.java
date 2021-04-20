package com.example.android_3d_loader.view.util;

import java.util.List;

public class ListUtil {

    public static<T> int getIndex(List<T> objectList, Object object){
        int index = 0;
        for (int i = 0; i < objectList.size(); i++){
            if (objectList.get(i).equals(object)){
                return index;
            }else {
                index++;
            }
        }
        return -1;
    }
}
