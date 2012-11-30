/*
 * Copyright (C) 2012 Colleage of Software Engineering, Southeast University
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.edu.seu.cose.jellyjolly.controller.tag;

import cn.edu.seu.cose.jellyjolly.dao.BlogPostDataAccess;
import cn.edu.seu.cose.jellyjolly.dto.BlogPost;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class GetPostTitleTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(
            GetPostTitleTag.class.getName());
    private static final ApplicationContext ctx =
            new ClassPathXmlApplicationContext(
            "cn/edu/seu/cose/jellyjolly/dist/applicationContext.xml");
    private BlogPostDataAccess blogPostDataAccess;
    private long postId;

    public GetPostTitleTag() {
        blogPostDataAccess = (BlogPostDataAccess) ctx.getBean(
                "blogPostDataAccess");
    }

    @Override
    public int doStartTag() throws JspException {
        return TagSupport.EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            BlogPost post = blogPostDataAccess.getPostById(postId);
            String title = post.getTitle();
            JspWriter out = pageContext.getOut();
            out.print(title);
            return TagSupport.EVAL_PAGE;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new JspException(ex);
        }
    }

    public void setPostId(Object postId) {
        this.postId = (Long) postId;
    }
}
