package tn.crns.ecos.outils;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table( name = "FILE_STORAGE" )
@NamedQueries( 
{
    @NamedQuery( name = "FileStorage.findAll", query = "SELECT f FROM FileStorageEntity f" ),
    @NamedQuery( name = "FileStorage.findByFsPk", query = "SELECT f FROM FileStorageEntity f WHERE f.fsPk = :fsPk" ),
    @NamedQuery( name = "FileStorage.findByFileName", query = "SELECT f FROM FileStorageEntity f WHERE f.fileName = :fileName" )
} )
public class FileStorageEntity implements Serializable
{
    private static final long serialVersionUID = -4796720242338042828L;
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "FS_PK" )
    private Long fsPk;
    
    
    @Column( name = "FILE_TYPE" )
    private String contentType;


	@Column( name = "FILE_NAME" )
    private String fileName;
    
    @Lob
    @Column( name = "CONTENT" ,columnDefinition="MEDIUMBLOB")
    private byte[] content;
    
 
    public FileStorageEntity()
    {
    }
 
    public FileStorageEntity( String type, String fileName, byte[] content)
    {
        this.fileName = fileName;
        this.content = content;
        this.contentType=type;
    }
 
    public Long getFsPk()
    {
        return fsPk;
    }
 
    public void setFsPk( Long fsPk )
    {
        this.fsPk = fsPk;
    }
 
    public String getFileName()
    {
        return fileName;
    }
 
    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }
 
    public byte[] getContent()
    {
        return content;
    }
 
    public void setContent( byte[] content )
    {
        this.content = content;
    }
 
    
    public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (fsPk != null ? fsPk.hashCode() : 0);
        return hash;
    }
 
    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !(object instanceof FileStorageEntity) )
        {
            return false;
        }
        FileStorageEntity other = ( FileStorageEntity ) object;
        if ( (this.fsPk == null && other.fsPk != null) || (this.fsPk != null && !this.fsPk.equals( other.fsPk )) )
        {
            return false;
        }
        return true;
    }
 
    @Override
    public String toString()
    {
        return "com.developerscrappad.fs.entity.FileStorage[ fsPk=" + fsPk + " ]";
    }

}
