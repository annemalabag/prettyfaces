package com.ocpsoft.pretty.faces.url;

import java.util.Iterator;
import java.util.List;

public class Metadata
{
   private String encoding = "UTF-8";
   private boolean trailingSlash = false;
   private boolean leadingSlash = false;

   /**
    * Return a copy of this Metadata
    */
   public Metadata copy()
   {
      Metadata result = new Metadata();
      result.setEncoding(encoding);
      result.setTrailingSlash(trailingSlash);
      result.setLeadingSlash(leadingSlash);
      return result;
   }

   public String buildURLFromSegments(final List<String> segments)
   {
      StringBuilder result = new StringBuilder();

      if (hasLeadingSlash())
      {
         result.append("/");
      }

      for (Iterator<String> iter = segments.iterator(); iter.hasNext();)
      {
         String segment = iter.next();
         result.append(segment);
         if (iter.hasNext())
         {
            result.append("/");
         }
      }

      if (hasTrailingSlash() && !result.toString().endsWith("/"))
      {
         result.append("/");
      }
      return result.toString();
   }

   /*
    * Getters & Setters
    */

   public String getEncoding()
   {
      return encoding;
   }

   public void setEncoding(final String encoding)
   {
      this.encoding = encoding;
   }

   public boolean hasTrailingSlash()
   {
      return trailingSlash;
   }

   public void setTrailingSlash(final boolean trailingSlash)
   {
      this.trailingSlash = trailingSlash;
   }

   public boolean hasLeadingSlash()
   {
      return leadingSlash;
   }

   public void setLeadingSlash(final boolean leadingSlash)
   {
      this.leadingSlash = leadingSlash;
   }
}
