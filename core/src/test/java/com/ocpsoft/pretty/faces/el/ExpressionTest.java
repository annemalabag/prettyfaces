package com.ocpsoft.pretty.faces.el;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ocpsoft.pretty.faces.el.Expressions;

public class ExpressionTest
{
   @Test
   public void testEmpty() throws Exception
   {
      assertFalse(Expressions.isEL(""));
      assertFalse(Expressions.containsEL(""));
   }

   @Test
   public void testNotAnExpression() throws Exception
   {
      assertFalse(Expressions.isEL("Not an expression."));
      assertFalse(Expressions.containsEL("Not an expression."));
   }

   @Test
   public void testEmbeddedExpression() throws Exception
   {
      assertFalse(Expressions.isEL("This contains an #{expression}."));
      assertTrue(Expressions.containsEL("This contains an #{expression}."));
   }

   @Test
   public void testErroneousExpressionLeft() throws Exception
   {
      assertFalse(Expressions.isEL("#{expre{ssion}"));
   }

   @Test
   public void testErroneousExpressionRight() throws Exception
   {
      assertFalse(Expressions.isEL("#{expre}ssion}"));
      assertTrue(Expressions.containsEL("#{expre}ssion}"));
      assertTrue(Expressions.containsEL("This contains an #{expre}ssion}."));
   }

   @Test
   public void testIsExpression() throws Exception
   {
      assertTrue(Expressions.isEL("#{this.is.an.expression}"));
      assertTrue(Expressions.containsEL("#{this.is.an.expression}"));
   }
}
