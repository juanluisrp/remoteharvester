package net.geocat.service;

import net.geocat.database.linkchecker.entities.Link;

public interface ILinkProcessor {

     Link process(Link link) throws  Exception;

}
