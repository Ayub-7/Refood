package org.seng302.models;

import lombok.Data;
import org.seng302.exceptions.InvalidImageExtensionException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class that represents an Image object.
 */
@Data
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String filename;
    private String thumbnailFilename;

    public Image(String filename, String thumbnailFilename) {
        this.filename = filename;
        this.thumbnailFilename = thumbnailFilename;
    }

    protected Image() {}

    /**
     * Checks the content type given and returns the extension string of the type.
     * @param contentType string extension to be found.
     * @return string representation of the file extension.
     * @throws InvalidImageExtensionException throw exception if the contentType (or rather, the file) is not of a valid extension.
     */
    public static String getContentTypeExtension(String contentType) throws InvalidImageExtensionException {
        switch (contentType) {
            case "image/png":
                return ".png";
            case "image/jpeg":
                return ".jpg";
            case "image/gif":
                return ".gif";
        }
        throw new InvalidImageExtensionException("Unsupported extension type.");
    }

    public long getId() {
        return this.id;
    }

}
