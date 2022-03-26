package com.github.xinput.commons.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.ByteConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.converters.basic.ShortConverter;
import com.thoughtworks.xstream.converters.basic.StringConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

import java.io.Writer;

/**
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 */
public class XStreamInitializer {
  private static final XppDriver XPP_DRIVER = new XppDriver() {
    @Override
    public HierarchicalStreamWriter createWriter(Writer out) {
      return new PrettyPrintWriter(out, this.getNameCoder()) {
        private static final String PREFIX_CDATA = "<![CDATA[";
        private static final String SUFFIX_CDATA = "]]>";
        private static final String PREFIX_MEDIA_ID = "<MediaId>";
        private static final String SUFFIX_MEDIA_ID = "</MediaId>";

        @Override
        protected void writeText(QuickWriter writer, String text) {
          if (text.startsWith("<![CDATA[") && text.endsWith("]]>")) {
            writer.write(text);
          } else if (text.startsWith("<MediaId>") && text.endsWith("</MediaId>")) {
            writer.write(text);
          } else {
            super.writeText(writer, text);
          }
        }

        @Override
        public String encodeNode(String name) {
          return name;
        }
      };
    }
  };

  public XStreamInitializer() {
  }

  public static XStream getInstance() {
    XStream xstream = new XStream(new PureJavaReflectionProvider(), XPP_DRIVER) {
      @Override
      protected void setupConverters() {
        this.registerConverter(new NullConverter(), 10000);
        this.registerConverter(new IntConverter(), 0);
        this.registerConverter(new FloatConverter(), 0);
        this.registerConverter(new DoubleConverter(), 0);
        this.registerConverter(new LongConverter(), 0);
        this.registerConverter(new ShortConverter(), 0);
        this.registerConverter(new BooleanConverter(), 0);
        this.registerConverter(new ByteConverter(), 0);
        this.registerConverter(new StringConverter(), 0);
        this.registerConverter(new DateConverter(), 0);
        this.registerConverter(new CollectionConverter(this.getMapper()), 0);
        this.registerConverter(new ReflectionConverter(this.getMapper(), this.getReflectionProvider()), -20);
      }
    };
    xstream.ignoreUnknownElements();
    xstream.setMode(1001);
    XStream.setupDefaultSecurity(xstream);
    xstream.autodetectAnnotations(true);
    xstream.addPermission(NoTypePermission.NONE);
//        xstream.addPermission(new WildcardTypePermission(new String[]{"com.precisource.**"}));
    xstream.setClassLoader(Thread.currentThread().getContextClassLoader());
    return xstream;
  }
}
