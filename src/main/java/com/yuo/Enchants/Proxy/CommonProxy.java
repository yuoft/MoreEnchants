package com.yuo.Enchants.Proxy;

public class CommonProxy implements IProxy {
    private static boolean IS_KEY_C = false; //是否按住快捷键

    /**
     * 切换按键状态
     * @param flag 是否按住
     */
    public static void setKeyC(boolean flag){
        IS_KEY_C = flag;
    }

    public static boolean isIsKeyC() {
        return IS_KEY_C;
    }
    @Override
    public void registerHandlers() {
    }
}
