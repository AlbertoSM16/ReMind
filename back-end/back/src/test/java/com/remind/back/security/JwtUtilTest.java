package com.remind.back.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "SECRET", "TestSecretKeyForHS256ThatIsLongEnoughToWork12345678");
        jwtUtil.init();
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtUtil.generateToken("testuser", "administrador");
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testGetUsernameFromToken() {
        String token = jwtUtil.generateToken("testuser", "administrador");
        assertEquals("testuser", jwtUtil.getUsernameFromToken(token));
    }

    @Test
    void testGetRoleFromToken() {
        String token = jwtUtil.generateToken("testuser", "terapeuta");
        assertEquals("terapeuta", jwtUtil.getRoleFromToken(token));
    }

    @Test
    void testValidateInvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    void testValidateMalformedToken() {
        assertFalse(jwtUtil.validateToken(""));
        assertFalse(jwtUtil.validateToken("abc.def"));
    }
}
