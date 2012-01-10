package com.ocpsoft.pretty.faces.util;

/**
 * Helper class providing similar functions like the StringUtils class of Apache
 * Commons.
 * 
 * @author Christian Kaltepoth
 */
public class StringUtils
{

   private static final char SLASH = '/';

   public static boolean isBlank(String s)
   {
      return s == null || s.trim().length() == 0;
   }

   public static boolean isNotBlank(String s)
   {
      return s != null && s.trim().length() > 0;
   }

   public static String trimToNull(String s)
   {
      return s == null || s.trim().length() == 0 ? null : s.trim();
   }

   public static boolean hasLeadingSlash(final String s)
   {
      return s != null && !s.isEmpty() && SLASH == s.charAt(0);
   }

   public static boolean hasTrailingSlash(final String s)
   {
      return s != null && !s.isEmpty() && SLASH == s.charAt(s.length() - 1);
   }

   public static String[] splitBySlash(final String s)
   {
      if (s == null || s.isEmpty())
      {
         return new String[0];
      }
      char[] chars = s.toCharArray();
      int numberOfSegments = countSlashes(s) + 1;

      final String[] segments = new String[numberOfSegments];
      int currentSegmentIndex = 0;
      int lastSlashIndex = -1;
      for (int i = 0; i < chars.length; ++i)
      {
         if (chars[i] == '/')
         {
            segments[currentSegmentIndex] = new String(chars, lastSlashIndex + 1, i - lastSlashIndex - 1);
            ++currentSegmentIndex;
            lastSlashIndex = i;
         }
      }
      if (lastSlashIndex + 1 < chars.length)
      {
         segments[currentSegmentIndex] = new String(chars, lastSlashIndex + 1, chars.length - lastSlashIndex - 1);
      }
      else
      {
         segments[currentSegmentIndex] = "";
      }

      return segments;

   }

   public static int countSlashes(final String s)
   {
      int result = 0;
      for (char ch : s.toCharArray())
      {
         if (ch == '/')
         {
            ++result;
         }
      }
      return result;
   }

}
