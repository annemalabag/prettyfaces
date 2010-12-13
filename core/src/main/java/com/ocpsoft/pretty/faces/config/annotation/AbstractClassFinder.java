/*
 * Copyright 2010 Lincoln Baxter, III
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.pretty.faces.config.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for implementations of the {@link ClassFinder} interface.
 * 
 * @see ClassFinder
 * @author Christian Kaltepoth
 */
public abstract class AbstractClassFinder implements ClassFinder
{

   /**
    * Common logger for all implementations
    */
   protected final Log log = LogFactory.getLog(this.getClass());

   /**
    * The {@link ServletContext}
    */
   protected final ServletContext servletContext;

   /**
    * The {@link ClassLoader} to get classes from
    */
   protected final ClassLoader classLoader;

   /**
    * The filter for checking which classes to process
    */
   protected final PackageFilter packageFilter;
   
   /**
    * The filter to check bytecode for interesting annotations
    */
   private final ByteCodeAnnotationFilter byteCodeAnnotationFilter;

   /**
    * Initialization procedure
    * 
    * @param servletContext
    *           The {@link ServletContext} of the web application.
    * @param classLoader
    *           The {@link ClassLoader} to use for loading classes
    * @param packageFilter
    *           The {@link PackageFilter} used to check if a package has to be
    *           scanned.
    */
   public AbstractClassFinder(ServletContext servletContext, ClassLoader classLoader, PackageFilter packageFilter)
   {
      this.servletContext = servletContext;
      this.classLoader = classLoader;
      this.packageFilter = packageFilter;
      this.byteCodeAnnotationFilter = new ByteCodeAnnotationFilter();
   }

   /**
    * Creates a webapp relative name from an URL. This method requires the
    * caller do supply a known prefix of the resulting name. Class files for
    * example would have a prefix like <code>/WEB-INF/classes</code>
    * 
    * @param prefix
    *           The first part of the resulting name.
    * @param url
    *           An {@link URL} pointing to a webapp resources
    * @return the webapp relative name
    */
   protected static String getWebappRelativeName(URL url, String prefix)
   {

      // we are only interested in the path
      String directoryPath = url.getPath();

      // we remove everything up to the prefix
      // (Example for the prefix: "/WEB-INF/classes/")
      int startIndex = directoryPath.lastIndexOf(prefix);

      // prefix not found?
      if (startIndex == -1)
      {
         throw new IllegalArgumentException("Cannot find prefix '" + prefix + "' in URL: " + url.toString());
      }

      return directoryPath.substring(startIndex);

   }

   /**
    * <p>
    * Creates a FQCN from an {@link URL} representing a <code>.class</code>
    * file.
    * </p>
    * <p>
    * You may optionally supply a prefix. In this case all characters up to the
    * prefix and the prefix itself will be removed from the file name. For
    * classes in a web application you may for example use
    * <code>/WEB-INF/lib/</code> as a prefix.
    * </p>
    * 
    * @param url
    *           The path of the class file
    * @param prefix
    *           optional prefix
    * @return the FQCN of the class
    */
   protected static String getClassName(String filename, String prefix)
   {

      // start index
      int startIndex = 0;

      // optional: remove prefix
      if (prefix != null && prefix.length() > 0)
      {

         // find prefix position
         int prefixLastIndex = filename.lastIndexOf(prefix);

         // prefix not found?
         if (prefixLastIndex == -1)
         {
            throw new IllegalArgumentException("Cannot find prefix '" + prefix + "' in filename: " + filename);
         }

         // set correct startIndex
         startIndex = prefixLastIndex + prefix.length();

      }

      // end index is just before ".class"
      int endIndex = filename.length() - ".class".length();

      // extract relevant part of the path
      String relativePath = filename.substring(startIndex, endIndex);

      // replace / by . to create FQCN
      return relativePath.replace('/', '.');
   }

   /**
    * Checks if a supplied class has to be processed by checking the package
    * name against the {@link PackageFilter}.
    * 
    * @param className
    *           FQCN of the class
    * @return <code>true</code> for classes to process, <code>false</code> for
    *         classes to ignore
    */
   protected boolean mustProcessClass(String className)
   {

      // the default package
      String packageName = "";

      // find last dot in class name to determine the package name
      int packageEndIndex = className.lastIndexOf(".");
      if (packageEndIndex != -1)
      {
         packageName = className.substring(0, packageEndIndex);
      }

      // check filter
      return packageFilter.isAllowedPackage(packageName);

   }

   /**
    * <p>
    * Handle a single class to process. This method should only be called if the
    * class name is accepted by the {@link PackageFilter}.
    * </p>
    * <p>
    * If <code>classFileStream</code> is not <code>null</code> the method will first
    * try to check whether the class files may contain annotations by scanning
    * it with the {@link ByteCodeAnnotationFilter}. If no {@link InputStream} is
    * supplied, this check will be skipped. After that the method will create an
    * instance of the class and then call
    * {@link PrettyAnnotationHandler#processClass(Class)}.
    * </p>
    * <p>
    * Please not the the called of this method is responsible to close the
    * supplied {@link InputStream}!
    * </p>
    * 
    * @param className
    *           The FQCN of the class
    * @param classFileStream
    *           The Java class file of the class (may be <code>null</code>)
    * @param handler
    *           the handler to notify
    */
   protected void processClass(String className, InputStream classFileStream, PrettyAnnotationHandler handler)
   {

      // bytecode check is only performed if the InputStream is available 
      if (classFileStream != null)
      {
         
         // we must take care of IOExceptions thrown by ByteCodeAnnotationFilter
         try
         {

            // call bytecode filter
            boolean shouldScanClass = byteCodeAnnotationFilter.accept(classFileStream);

            // No annotations -> abort
            if (!shouldScanClass)
            {
               return;
            }

            // filter says we should scan the class
            if (log.isDebugEnabled())
            {
               log.debug("Bytecode filter recommends to scan class: " + className);
            }
            
         }
         catch (IOException e)
         {
            if (log.isDebugEnabled())
            {
               log.debug("Failed to parse class file: " + className, e);
            }
         }
      }
      
      try
      {
         // request this class from the ClassLoader
         Class<?> clazz = classLoader.loadClass(className);

         // call handler
         handler.processClass(clazz);

      }
      catch (NoClassDefFoundError e)
      {
         // reference to another class unknown to the classloader
         log.debug("Could not load class '" + className + "': " + e.toString());
      }
      catch (ClassNotFoundException e)
      {
         // should no happen, because we found the class on the classpath
         throw new IllegalStateException("Unable to load class: " + className, e);
      }

   }

}
