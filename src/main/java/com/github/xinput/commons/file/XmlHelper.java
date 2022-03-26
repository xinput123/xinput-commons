package com.github.xinput.commons.file;

import com.github.xinput.commons.xml.XStreamInitializer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * 解析xml数据
 */
public class XmlHelper {
  /**
   * java 转换成xml
   *
   * @param obj 对象实例
   * @return String xml字符串
   */
  public static String toXml(Object obj) {
    XStream xstream = XStreamInitializer.getInstance();
    // 如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
    // 通过注解方式的，一定要有这句话
    xstream.processAnnotations(obj.getClass());
    return xstream.toXML(obj);
  }

  /**
   * 将传入xml文本转换成Java对象
   *
   * @param xml
   * @param cls xml对应的class类
   * @return T   xml对应的class类的实例对象
   */
  @SuppressWarnings("unchecked")
  public static <T> T toBean(String xml, Class<T> cls) {
    // 注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
    XStream xstream = new XStream(new DomDriver()) {
      @Override
      protected MapperWrapper wrapMapper(MapperWrapper next) {
        return new MapperWrapper(next) {
          @Override
          public boolean shouldSerializeMember(Class definedIn, String fieldName) {
            if (definedIn == Object.class) {
              return false;
            }
            return super.shouldSerializeMember(definedIn, fieldName);
          }
        };
      }
    };
    xstream.processAnnotations(cls);
    XStream.setupDefaultSecurity(xstream);
    xstream.allowTypes(new Class[]{cls});
    return (T) xstream.fromXML(xml);
  }

  @SuppressWarnings("unchecked")
  public static <T> T toBean(Object[] objs, Class<T> clazz) {
    // 注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
    XStream xstream = new XStream(new DomDriver()) {
      @Override
      protected MapperWrapper wrapMapper(MapperWrapper next) {
        return new MapperWrapper(next) {
          @Override
          public boolean shouldSerializeMember(Class definedIn, String fieldName) {
            if (definedIn == Object.class) {
              return false;
            }
            return super.shouldSerializeMember(definedIn, fieldName);
          }
        };
      }
    };
    xstream.processAnnotations(clazz);
    XStream.setupDefaultSecurity(xstream);
    xstream.allowTypes(new Class[]{clazz});
    return (T) xstream.fromXML(String.valueOf(objs[0]));
  }
}
