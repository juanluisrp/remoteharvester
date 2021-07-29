package net.geocat.service;

import net.geocat.database.linkchecker.entities2.Link;

public interface ILinkProcessor {

     Link process(Link link) throws  Exception;

}
