package sistemas.edu.pe.sistemaescaneo.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import sistemas.edu.pe.sistemaescaneo.entity.Usuario;
import sistemas.edu.pe.sistemaescaneo.service.IUsuarioService;

@Service
public class JwtUtil {
    private  String SECRET_KEY ="holamellamokatherinemiravalcabrerayestaesunaclavemuysecreta";

    @Autowired
	private IUsuarioService usuarioService;
	
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
    	 return extractClaim(token, Claims::getExpiration);
    }
 
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    	final Claims claims = extractAllClaims(token);
    	return claimsResolver.apply(claims);
    }
    
	private Claims extractAllClaims(String token) {
    	return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }
    
    private Boolean isTokenExpired(String token) {
    	return extractExpiration(token).before(new Date());
    }
    
    public String generateToken(UserDetails userDetails) {
    	Map<String, Object> claims = new HashMap<>();
    	Usuario usuario = usuarioService.findByUsername(userDetails.getUsername());
    	claims.put("nombre", usuario.getPersona().getNombre());
    	claims.put("apellidopat", usuario.getPersona().getApellidopat());
    	claims.put("apellidomat", usuario.getPersona().getApellidomat());
    	return createToken(claims, userDetails.getUsername());
    }
    
	private String createToken(Map<String, Object> claims, String subject) {
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
		System.out.print(claims);
    	return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
    			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10 ))
    			.signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
    	final String username = extractUsername(token);
    	return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
    }
}
