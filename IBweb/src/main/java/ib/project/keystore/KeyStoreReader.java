package ib.project.keystore;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import ib.project.certificate.model.IssuerData;

/**
 * 
 * Cita podatke iz keystore fajla
 */
public class KeyStoreReader {

	private Certificate cert = null;

	public IssuerData readKeyStore(String keyStoreFile, String alias, char[] password, char[] keyPass)
			throws ParseException {
		IssuerData issuer = null;
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, password);
			// citamo par sertifikat privatni kljuc
			System.out.println("Cita se Sertifikat...");
			System.out.println("Učitani ertifikat:");
			cert = ks.getCertificate(alias);
			System.out.println(cert);
			PrivateKey privKey = (PrivateKey) ks.getKey(alias, keyPass);

			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
			issuer = new IssuerData(privKey, issuerName);

		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return issuer;

	}

	public PublicKey readPublicKey() {
		return cert.getPublicKey();
	}

}