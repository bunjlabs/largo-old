package com.bunjlabs.largo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileLargoLoader extends AbstractLargoLoader{
    @Override
    Reader getSourceReader(String id) throws LargoLoaderException{
        try {
            return new FileReader(id);
        } catch (FileNotFoundException e) {
            throw new LargoLoaderException(e);
        }
    }
}
