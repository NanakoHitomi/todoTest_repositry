package controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class UsersTest {
	
	@Test
    public void testTrueContent() throws Exception {
        // 20文字のコンテント
        boolean result = Users.checklength("abcdefghijklmnopqrst");
        // 結果がtrueであることを確認
        assertTrue(result);
    }

	@Test
	 public void testShortContent() throws Exception {
        // 2文字のコンテント
        boolean result = Users.checklength("ab");
        // 結果がfalseであることを確認
        assertFalse(result);
    }
	
	@Test
	 public void testLongContent() throws Exception {
       // 21文字のコンテント
       boolean result = Users.checklength("abcdefghijklmnopqrstu");
       // 結果がfalseであることを確認
       assertFalse(result);
   }
	
	
}