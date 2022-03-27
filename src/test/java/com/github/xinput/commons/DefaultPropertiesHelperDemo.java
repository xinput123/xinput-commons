package com.github.xinput.commons;

import com.github.xinput.commons.file.DefaultPropertiesHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DefaultPropertiesHelper} 示例
 *
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 */
public class DefaultPropertiesHelperDemo {

  private static final Logger logger = LoggerFactory.getLogger(DefaultPropertiesHelper.class);

  @Test
  public void test01() {
    logger.info("id:{}", DefaultPropertiesHelper.getInt("id"));
    logger.info("list:{}", DefaultPropertiesHelper.getList("list"));
  }
}
