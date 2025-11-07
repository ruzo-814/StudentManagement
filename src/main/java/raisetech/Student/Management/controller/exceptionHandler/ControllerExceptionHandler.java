package raisetech.student.management.controller.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import raisetech.student.management.exception.GetIdTestException;
import raisetech.student.management.exception.GetTestException;
import raisetech.student.management.exception.PostTestException;

@RestControllerAdvice
public class ControllerExceptionHandler {


  /**
   * 受講生一覧検索の例外処理用のメソッドです。
   *
   * @param ex 無効なURL
   * @return 入力されたURLが間違っていた場合の例外処理
   */
  @ExceptionHandler(GetTestException.class)
  public ResponseEntity<String> getTestException(GetTestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }


  /**
   * 受講生検索の例外処理用のメソッドです
   *
   * @param ex 論理削除されたユーザーID
   * @return 入力されたユーザーIDが論理削除されていた場合の例外処理
   */
  @ExceptionHandler(GetIdTestException.class)
  public  ResponseEntity<String> getIdTestException(GetIdTestException ex){
    return  ResponseEntity.status(HttpStatus.GONE).body(ex.getMessage());
  }


  /**
   * 新規受講生登録の例外処理用のメソッドです
   *
   * @param ex gender入力欄での[male/female/other]以外の入力
   * @return 入力された内容がバリデーションに引っかかった場合の例外処理
   */
  @ExceptionHandler(PostTestException.class)
  public ResponseEntity<String> postTestException(PostTestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
