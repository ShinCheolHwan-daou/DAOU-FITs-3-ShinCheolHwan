package booksearchmvc.controller;

import booksearchmvc.service.BookSearchMVCService;
import booksearchmvc.service.BookSearchMVCServiceOracleImpl;
import booksearchmvc.vo.BookVO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class BookSearchMVCController implements Initializable {

    @FXML
    private TableView<BookVO> tableView;
    @FXML
    private TableColumn<BookVO, String> isbnCol;
    @FXML
    private TableColumn<BookVO, String> titleCol;
    @FXML
    private TableColumn<BookVO, Integer> priceCol;
    @FXML
    private TableColumn<BookVO, String> authorCol;
    @FXML
    private TextField textField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button deleteBtn;

    public BookSearchMVCController() {
        BookSearchMVCService service = new BookSearchMVCServiceOracleImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setEditable(true);
        // View의 이벤트 등록
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("bisbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("btitle"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("bprice"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("bauthor"));

        // 수정 가능하도록 setCellFactory 추가
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        authorCol.setCellFactory(TextFieldTableCell.forTableColumn());
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // 겁색 버튼이 클릭되면 키워드를 이용한 검색 작업을 진행
        searchBtn.setOnAction(event -> {
            try {
                BookSearchMVCService service = new BookSearchMVCServiceOracleImpl();
                ObservableList<BookVO> list = service.searchBookByKeyword(textField.getText());
                tableView.setItems(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        deleteBtn.setOnAction(event -> {
            BookVO book = tableView.getSelectionModel().getSelectedItem();
            if (book == null) {
                System.out.println("삭제할 책을 선택하세요.");
                return;
            }

            try {
                BookSearchMVCService service = new BookSearchMVCServiceOracleImpl();
                boolean result = service.deleteBook(book.getBisbn());

                if (result) {
                    System.out.println("삭제 성공 " + book.getBisbn());
                    tableView.getItems().remove(book);
                } else {
                    System.out.println("삭제 실패 " + book.getBisbn());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        titleCol.setOnEditCommit(event -> {
            BookVO book = event.getRowValue();
            String newValue = event.getNewValue();
            book.setBtitle(newValue);

            try {
                BookSearchMVCService service = new BookSearchMVCServiceOracleImpl();
                boolean result = service.updateBook(book);
                if (result) {
                    System.out.println("업데이트 성공 " + book.getBtitle());
                } else {
                    System.out.println("업데이트 실패 " + book.getBtitle());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        priceCol.setOnEditCommit(event -> {
            BookVO book = event.getRowValue();
            int newValue = event.getNewValue();
            book.setBprice(newValue);

            try {
                BookSearchMVCService service = new BookSearchMVCServiceOracleImpl();
                boolean result = service.updateBook(book);
                if (result) {
                    System.out.println("업데이트 성공 " + book.getBprice());
                } else {
                    System.out.println("업데이트 실패 " + book.getBprice());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        authorCol.setOnEditCommit(event -> {
            BookVO book = event.getRowValue();
            String newValue = event.getNewValue();
            book.setBauthor(newValue);

            try {
                BookSearchMVCService service = new BookSearchMVCServiceOracleImpl();
                boolean result = service.updateBook(book);
                if (result) {
                    System.out.println("업데이트 성공 " + book.getBauthor());
                } else {
                    System.out.println("업데이트 실패 " + book.getBauthor());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
