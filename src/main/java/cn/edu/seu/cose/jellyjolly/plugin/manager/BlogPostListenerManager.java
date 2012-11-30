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

package cn.edu.seu.cose.jellyjolly.plugin.manager;

import cn.edu.seu.cose.jellyjolly.plugin.listener.BlogPostListener;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class BlogPostListenerManager
        implements ListenerManager<BlogPostListener> {
    
    private Set<BlogPostListener> blogPostListeners;
    
    public BlogPostListenerManager() {
        blogPostListeners = new HashSet<BlogPostListener>();
    }

    @Override
    public Set<BlogPostListener> getListenerSet() {
        return blogPostListeners;
    }

    @Override
    public void register(BlogPostListener listener) {
        blogPostListeners.add(listener);
    }

}
