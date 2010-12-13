/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER3 or visit the
 * GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.config;

import java.util.LinkedList;
import java.util.List;

import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;

/**
 * Pretty Faces configuration builder. Accepts configuration elements (
 * {@link #addMapping(UrlMapping)}) and builds the configuration (
 * {@link #build()}).
 * 
 * @author Aleksei Valikov
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PrettyConfigBuilder
{
   private final List<UrlMapping> mappings = new LinkedList<UrlMapping>();
   private final List<RewriteRule> rewriteRules = new LinkedList<RewriteRule>();

   public void addFromConfig(PrettyConfig config)
   {
      if (config != null)
      {
         mappings.addAll(config.getMappings());
         rewriteRules.addAll(config.getGlobalRewriteRules());
      }
   }

   public void addMapping(final UrlMapping mapping)
   {
      if (mapping != null)
      {
         mappings.add(mapping);
      }
   }

   public void addRewriteRule(final RewriteRule rule)
   {
      if (rule != null)
      {
         rewriteRules.add(rule);
      }
   }

   public PrettyConfig build()
   {
      final PrettyConfig config = new PrettyConfig();

      config.setMappings(mappings);
      config.setGlobalRewriteRules(rewriteRules);

      return config;
   }
}
