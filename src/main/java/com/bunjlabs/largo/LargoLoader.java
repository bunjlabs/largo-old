package com.bunjlabs.largo;

import java.io.Reader;

public interface LargoLoader {

    Blueprint load(String id) throws LargoLoaderException;

    Blueprint loadSource(String source) throws LargoLoaderException;

    Blueprint loadSource(Reader source) throws LargoLoaderException;
}
