package utilisateurTests;

import org.junit.Before;
import org.junit.Test;

import com.sigma.model.Address;
import com.sigma.model.Entite;
import com.sigma.model.UserType;
import com.sigma.model.Visiteur;
import com.sigma.utilisateur.Utilisateur;

import java.util.Date;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class utilisateurTest {

    private Utilisateur utilisateur;

    @Before
    public void setUp() throws Exception {
        utilisateur = new Utilisateur();
    }

    @Test
    public void TestConstructeurUtlisateurAvecParametreID() throws Exception {
        //Given
         long id=1000;
         
        //When
		utilisateur=new Utilisateur( id);
        
        //Then
        assertThat(utilisateur.getId()).isEqualTo(id);
    }
    
    @Test
    public void TestConstructeurAvecParametre2eme() throws Exception {
    	
        //Given
        String nom;
        String prenom;
        String mail;
        Address adresse;
        String telephone;
        String mobile;
		String password;
		Date dateEnregistrement;
		UserType userType;
        //When
		nom="albert";
		prenom="prnm";
		mail="mimo@gmail.com";
		adresse=new Address("107", "pascal", "76000","Rouen","France");
		telephone="07777777";
	    mobile="07777777";
		password="mmmm";
		dateEnregistrement=new Date();
		userType=UserType.ADMINENTITY ;
		utilisateur=new Utilisateur( nom, prenom,  mail,  adresse,  telephone,  mobile,
			 password,  dateEnregistrement,  userType);
        
        
        //Then
        assertThat(utilisateur.getNom()).isEqualTo(nom);
        assertThat(utilisateur.getPrenom()).isEqualTo(prenom);
        assertThat(utilisateur.getMail()).isEqualTo(mail);
        assertThat(utilisateur.getAdresse()).isEqualTo(adresse);
        assertThat(utilisateur.getTelephone()).isEqualTo(telephone);
        assertThat(utilisateur.getMobile()).isEqualTo(mobile);
        assertThat(utilisateur.getPassword()).isEqualTo(password);
        assertThat(utilisateur.getDateEnregistrement()).isEqualTo(dateEnregistrement);
        assertThat(utilisateur.getUserType()).isEqualTo(userType );   
        
    }

    @Test
    public void TestConstructeurAvecParametre3eme() throws Exception {
    	
        //Given
        String nom;
        String prenom;
        String mail;
		String password;
        //When
		nom="albert";
		prenom="prnm";
		mail="mimo@gmail.com";
		password="mmmm";
		utilisateur=new Utilisateur( nom, prenom,  mail,password);
        
        //Then
        assertThat(utilisateur.getNom()).isEqualTo(nom);
        assertThat(utilisateur.getPrenom()).isEqualTo(prenom);
        assertThat(utilisateur.getMail()).isEqualTo(mail);
        assertThat(utilisateur.getPassword()).isEqualTo(password);
        
    }
    
    @Test
    public void TestSetNom() throws Exception {
    	
        //Given
        String nom;
        //When
		nom="albert";
		utilisateur=new Utilisateur( );
		utilisateur.setNom(nom);
        
        //Then
        assertThat(utilisateur.getNom()).isEqualTo(nom);
        
    }
    
    @Test
    public void TestSetPrenom() throws Exception {
    	
        //Given
        String prenom;
        //When
		prenom="mimo";
		utilisateur=new Utilisateur( );
		utilisateur.setPrenom(prenom);
        
        //Then
        assertThat(utilisateur.getPrenom()).isEqualTo(prenom);
        
    }
    @Test
    public void TestSetMail() throws Exception {
    	
        //Given
        String mail;
        //When
		mail="mimo@gmail.com";
		utilisateur=new Utilisateur( );
		utilisateur.setMail(mail);
        
        //Then
        assertThat(utilisateur.getMail()).isEqualTo(mail);
        
    }
    @Test
    public void TestSetAdresse() throws Exception {
    	
        //Given
        Address adresse;
        //When
		adresse=new Address("107", "pascal", "76000","Rouen","France");
		utilisateur=new Utilisateur( );
		utilisateur.setAdresse(adresse);
        
        //Then
        assertThat(utilisateur.getAdresse()).isEqualTo(adresse);
        
    }
    
    @Test
    public void TestSetTelephone() throws Exception {
    	
        //Given
        String telephone;
        //When
        telephone="07777777";
		utilisateur=new Utilisateur( );
		utilisateur.setTelephone(telephone);
        
        //Then
        assertThat(utilisateur.getTelephone()).isEqualTo(telephone);
        
    }
    
    @Test
    public void TestSetMobile() throws Exception {
    	
        //Given
        String mobile;
        //When
        mobile="07777777";
		utilisateur=new Utilisateur( );
		utilisateur.setMobile(mobile);
        
        //Then
        assertThat(utilisateur.getMobile()).isEqualTo(mobile);
        
    }
    @Test
    public void TestSetPassword() throws Exception {
    	
        //Given
        String password;
        //When
    	password="mmmm";
		utilisateur=new Utilisateur( );
		utilisateur.setPassword(password);
        
        //Then
        assertThat(utilisateur.getPassword()).isEqualTo(password);
        
    }
    @Test
    public void TestSetUserType() throws Exception {
    	
        //Given
        UserType userType;
        //When
    	userType=UserType.ADMINENTITY ;
		utilisateur=new Utilisateur( );
		utilisateur.setUserType(userType);
        
        //Then
        assertThat(utilisateur.getUserType()).isEqualTo(userType);
        
    }
    @Test
    public void TestSetDateEnregistrement() throws Exception {
    	
        //Given
    	Date dateEnregistrement;
        //When
    	dateEnregistrement=new Date();
		utilisateur=new Utilisateur();
		utilisateur.setDateEnregistrement(dateEnregistrement);
        
        //Then
        assertThat(utilisateur.getDateEnregistrement()).isEqualTo(dateEnregistrement);
        
    }
	

}