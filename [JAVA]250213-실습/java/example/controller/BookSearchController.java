package example.controller;

import example.service.BookSearchService;
import example.service.BookSearchServiceOracleImpl;
import example.vo.BookVO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class BookSearchController implements Initializable {
    @FXML
    private TableView<BookVO> bookTable;
    @FXML
    private TableColumn<BookVO, String> isbnCol;
    @FXML
    private TableColumn<BookVO, String> titleCol;
    @FXML
    private TableColumn<BookVO, Integer> priceCol;
    @FXML
    private TableColumn<BookVO, String> authorCol;
    @FXML
    private TextField isbnInput;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField priceInput;
    @FXML
    private TextField authorInput;
    @FXML
    private Button registerBtn;
    @FXML
    private TextField searchInput;
    @FXML
    private Button searchBtn;
    @FXML
    private Button deleteBtn;

    public BookSearchController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookTable.setEditable(true);
        // View의 이벤트 등록
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("bisbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("btitle"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("bprice"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("bauthor"));

        // 수정 가능하도록 setCellFactory 추가
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        authorCol.setCellFactory(TextFieldTableCell.forTableColumn());
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        searchBtn.setOnAction(event -> {
            try {
                BookSearchService service = new BookSearchServiceOracleImpl();
                ObservableList<BookVO> list = FXCollections.observableArrayList();
                list.setAll(service.searchBookByTitleKeyword(searchInput.getText()));
                bookTable.setItems(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        deleteBtn.setOnAction(event -> {
            BookVO book = bookTable.getSelectionModel().getSelectedItem();
            if (book == null) {
                System.out.println("삭제할 책을 선택하세요.");
                return;
            }

            try {
                BookSearchService service = new BookSearchServiceOracleImpl();
                boolean result = service.deleteBook(book.getBisbn());

                if (result) {
                    System.out.println("삭제 성공 " + book.getBisbn());
                    bookTable.getItems().remove(book);
                } else {
                    System.out.println("삭제 실패 " + book.getBisbn());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        registerBtn.setOnAction(event -> {
            String isbn = isbnInput.getText();
            String title = titleInput.getText();
            String price = priceInput.getText();
            String author = authorInput.getText();
            if (isbn.isEmpty() || title.isEmpty() || price.isEmpty() || author.isEmpty()) {
                System.out.println("공백이 존재합니다.");
                return;
            }

            if (!price.matches("^[0-9]+$")) {
                System.out.println("형식이 맞지 않습니다.");
                return;
            }

            BookVO newBook = new BookVO(isbn, title, Integer.parseInt(price), author);
            try {
                BookSearchService service = new BookSearchServiceOracleImpl();
                boolean result = service.createBook(newBook);

                if (result) {
                    System.out.println("생성 성공 " + newBook.getBisbn());
                    isbnInput.setText("");
                    titleInput.setText("");
                    priceInput.setText("");
                    authorInput.setText("");
                    ObservableList<BookVO> list = FXCollections.observableArrayList();
                    list.setAll(service.searchBookByTitleKeyword(searchInput.getText()));
                    bookTable.setItems(list);
                } else {
                    System.out.println("생성 실패 " + newBook.getBisbn());
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
                BookSearchService service = new BookSearchServiceOracleImpl();
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
                BookSearchService service = new BookSearchServiceOracleImpl();
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
                BookSearchService service = new BookSearchServiceOracleImpl();
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
