package com.ocpsoft.pretty;

/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see the file COPYING.LESSER or visit the GNU website at
 * <http://www.gnu.org/licenses/>.
 */
import javax.faces.FacesException;

/**
 * Extends FacesException to provide consistent exception behavior. This is a
 * Faces extension, after all
 * 
 * @author lb3
 */
public class PrettyException extends FacesException
{
    private static final long serialVersionUID = 1478911690378316003L;

    public PrettyException()
    {
        super();
    }

    public PrettyException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public PrettyException(final String message)
    {
        super(message);
    }

    public PrettyException(final Throwable cause)
    {
        super(cause);
    }
}
