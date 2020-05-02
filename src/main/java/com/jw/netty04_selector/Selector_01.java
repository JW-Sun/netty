package com.jw.netty04_selector;

/*21 22*/
/*
*  open获得选择器对象实例
*
*  public int select (long timeout)
*  监控所有注册的通道，当其中有IO操作可以进行的时候
*  将对应的SelectionKey加入到内部集合中并返回，参数又来设置超时时间
*
*  public Set<SelectionKey> selectedKeys()
*  从内部集合中得到所有的SelectionKey
* */
public class Selector_01 {

}
