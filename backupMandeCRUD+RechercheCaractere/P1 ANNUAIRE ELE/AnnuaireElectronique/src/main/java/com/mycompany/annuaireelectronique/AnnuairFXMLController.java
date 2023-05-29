/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.annuaireelectronique;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Inclusiv Academy 001
 */
public class AnnuairFXMLController implements Initializable {

    @FXML
    private Button bt_ajouter;
    @FXML
    private TextField tf_rechercher;
    @FXML
    private Button bt_rechercher;
    @FXML
    private Button bt_annuler;
    @FXML
    private TextField tf_nom;
    @FXML
    private TextField tf_prenom;
    @FXML
    private ComboBox<String> combo_genre;
    String[] genre = {"Masculin", "Féminin", "Autre"};
    @FXML
    private TextField tf_date;
    @FXML
    private TextField tf_localisation;
    @FXML
    private Label label;//titre
    @FXML
    private TextField tf_formation;
    @FXML
    private ComboBox<String> combo_secteur;
    String[] secteur = {"Établissements publics", "Établissements privés", "Autre"};
    @FXML
    private TableView<Student> tableview;
    @FXML
    private TableColumn<Student, Integer> col_id;
    @FXML
    private TableColumn<Student, String> col_nom;
    @FXML
    private TableColumn<Student, String> col_prenom;
    @FXML
    private TableColumn<Student, String> col_genre;
    @FXML
    private TableColumn<Student, String> col_date;
    @FXML
    private TableColumn<Student, String> col_localisation;
    @FXML
    private TableColumn<Student, String> col_formation;
    @FXML
    private TableColumn<Student, String> col_secteur;
    @FXML
    private Button bt_supprimer;
    @FXML
    private Button bt_modifier;
    @FXML
    private Label tf_total;//affichage du nombre d'element

    String firstLine;//premier ligne database
    String path = "C:\\Users\\natha\\Documents\\Project1_Annuaire\\backup mande Ajout annul suppr modif\\P1 ANNUAIRE ELE\\AnnuaireElectronique\\src\\main\\java\\com\\mycompany\\annuaireelectronique\\database.txt";

    //Lire le fichier txt, Creation list, insertion dans la liste
    public ArrayList<Student> liste_Init() {
        ArrayList<Student> list_student = new ArrayList<>();
        Student student;
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, "Cp863");//lire en codage UTF-8
            BufferedReader bf = new BufferedReader(isr);
            firstLine = bf.readLine();//ranger la premiere ligne
            //insertion dans la liste
            int i = 1;
            String line;
            while ((line = bf.readLine()) != null) {
                String[] decomp = line.split("\t");//decomposition d'une ligne
                student = new Student(decomp[5], decomp[6], decomp[4], decomp[0], decomp[1], decomp[2], decomp[3]);
                list_student.add(student);
                student.setId(i);
                i++;
            }
            bf.close();

        } catch (IOException e) {
            System.out.println("data input failed!");
        }
        return list_student;
    }
    ArrayList<Student> listInit = liste_Init();//list initial d'origine

    //afficher list sur tableview
    public void showList(ArrayList<Student> list_annuaire) {
        ObservableList<Student> liste = FXCollections.observableArrayList();

        for (Student temp : list_annuaire) {
            liste.add(temp);
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("dateEntry"));
        col_localisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        col_formation.setCellValueFactory(new PropertyValueFactory<>("formation"));
        col_secteur.setCellValueFactory(new PropertyValueFactory<>("secteur"));

        tableview.setItems(liste);
        tf_total.setText(String.valueOf(liste.size()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO    
        ObservableList<String> combolist_secteur = FXCollections.observableArrayList(secteur);
        combo_secteur.setItems(combolist_secteur);

        ObservableList<String> combolist = FXCollections.observableArrayList(genre);
        combo_genre.setItems(combolist);
        tf_total.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        tableview.setEditable(true);
        showList(listInit);

    }

    @FXML
    private void ajouterAction(ActionEvent event) throws IOException {
        if (!tf_nom.getText().isEmpty()
                && !tf_prenom.getText().isEmpty()
                && !tf_date.getText().isEmpty()
                && !tf_localisation.getText().isEmpty()
                && !tf_formation.getText().isEmpty()
                && !combo_genre.getValue().isEmpty()
                && !combo_secteur.getValue().isEmpty()) {
            ajouter();
        } else if (tf_nom.getText().isBlank()) {
            tf_nom.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");

        } else if (tf_prenom.getText().isBlank()) {
            tf_prenom.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");

        }

    }

    @FXML
    private void supprimerAction(ActionEvent event) throws IOException {
        if (tableview.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Suppression");
            alert.setHeaderText("Confirmer Suppression");
            alert.setContentText("Voulez-vous vraiment supprimer la selection ?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                supprimer();
                raz();
            } else {
                raz();
            }
            showList(listInit);
        } else {
            tableview.refresh();
        }
    }

    public void supprimer() throws IOException {
        Student studentselected = tableview.getSelectionModel().getSelectedItem();

        if (studentselected != null) {
            listInit.remove(studentselected);
            tableview.setItems(FXCollections.observableArrayList(listInit));
        }
        overwrite();

    }

    public void selectItem() {
        Student studentselected = tableview.getSelectionModel().getSelectedItem();
        if (studentselected != null) {
            tf_nom.setText(studentselected.getNom());
            tf_prenom.setText(studentselected.getPrenom());
            combo_genre.setValue(studentselected.getGenre());
            tf_date.setText(studentselected.getDateEntry());
            tf_localisation.setText(studentselected.getLocalisation());
            tf_formation.setText(studentselected.getFormation());
            combo_secteur.setValue(studentselected.getSecteur());
        }

    }

    public void modifier() throws IOException {
        Student studentupdated = tableview.getSelectionModel().getSelectedItem();

        studentupdated.setNom(tf_nom.getText());
        studentupdated.setPrenom(tf_prenom.getText());
        studentupdated.setGenre(combo_genre.getValue());
        studentupdated.setDateEntry(tf_date.getText());
        studentupdated.setLocalisation(tf_localisation.getText());
        studentupdated.setFormation(tf_formation.getText());
        studentupdated.setSecteur(combo_secteur.getValue());

        tableview.refresh(); // Rafraîchit la TableView avec les modifications
        System.out.println("Élément modifié avec succès");

        overwrite();
    }

    public void modifierSurtable() throws IOException {
        tableview.setEditable(true);
        col_nom.setCellFactory(TextFieldTableCell.forTableColumn());
        col_nom.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> ev) {
                Student studentupdated = ev.getRowValue();
                if (ev.getNewValue() != null) {
                    studentupdated.setNom(ev.getNewValue());
                }
            }
        });
        col_prenom.setCellFactory(TextFieldTableCell.forTableColumn());
        col_prenom.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> ev) {
                Student studentselect = ev.getRowValue();
                if (ev.getNewValue() != null) {
                    studentselect.setPrenom(ev.getNewValue());
                }
            }
        });
        col_genre.setCellFactory(TextFieldTableCell.forTableColumn());
        col_genre.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> ev) {
                Student studentselect = ev.getRowValue();
                if (ev.getNewValue() != null) {
                    studentselect.setGenre(ev.getNewValue());
                }
            }
        });
        col_date.setCellFactory(TextFieldTableCell.forTableColumn());
        col_date.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> ev) {
                Student studentselect = ev.getRowValue();
                if (ev.getNewValue() != null) {
                    studentselect.setDateEntry(ev.getNewValue());
                }
            }
        });
        col_localisation.setCellFactory(TextFieldTableCell.forTableColumn());
        col_localisation.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> ev) {
                Student studentselect = ev.getRowValue();
                if (ev.getNewValue() != null) {
                    studentselect.setLocalisation(ev.getNewValue());
                }
            }
        });
        col_formation.setCellFactory(TextFieldTableCell.forTableColumn());
        col_formation.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> ev) {
                Student studentselect = ev.getRowValue();
                if (ev.getNewValue() != null) {
                    studentselect.setFormation(ev.getNewValue());
                }
            }
        });
        col_secteur.setCellFactory(TextFieldTableCell.forTableColumn());
        col_secteur.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> ev) {
                Student studentselect = ev.getRowValue();
                if (ev.getNewValue() != null) {
                    studentselect.setSecteur(ev.getNewValue());
                }
            }
        });
    }

    @FXML
    private void modifierAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Modification");
        alert.setHeaderText("Confirmer Modification");
        alert.setContentText("Voulez-vous appliquer la modification ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            modifier();
        } else {
            raz();
        }
        showList(listInit);
    }
private void overwrite() throws FileNotFoundException, IOException {
        FileOutputStream txt = new FileOutputStream(path, false);
        OutputStreamWriter ots = new OutputStreamWriter(txt, Charset.forName("Cp1252"));
        BufferedWriter bfW = new BufferedWriter(ots);

        bfW.write(firstLine);
        bfW.newLine();
        for (Student student : listInit) {
            bfW.write(student.getDateEntry()
                    + "\t" + student.getLocalisation()
                    + "\t" + student.getFormation()
                    + "\t" + student.getSecteur()
                    + "\t" + student.getGenre()
                    + "\t" + student.getNom()
                    + "\t" + student.getPrenom());
            bfW.newLine();
        }

        bfW.close();

    }

    private void ajouter() throws IOException {
        //recuperation des données
        Student student = new Student();
        student.setNom(tf_nom.getText());
        student.setPrenom(tf_prenom.getText());
        student.setGenre(combo_genre.getValue().toString());
        student.setDateEntry(tf_date.getText());
        student.setLocalisation(tf_localisation.getText());
        student.setFormation(tf_formation.getText());
        student.setSecteur(combo_secteur.getValue());

        listInit.add(student);
        student.setId(listInit.lastIndexOf(student) + 1);
        overwrite();

        raz();
        showList(listInit);

    }

    
    
    @FXML
    private void selectedItem(MouseEvent event) throws IOException {
        if (event.getClickCount() == 1) {
            selectItem();
            tableview.refresh();
        } else {
            modifierSurtable();
            tableview.refresh();
            overwrite();
            raz();
        }
    }

    @FXML
    void pressedAction(MouseEvent event) {
    }

    @FXML
    private void annulerAction(ActionEvent event) {
        raz();
    }

    public void raz() {

        tf_nom.setText("");
        tf_prenom.setText("");
        combo_genre.setValue("");
        tf_date.setText("");
        tf_formation.setText("");
        combo_secteur.setValue("");
        tf_localisation.setText("");
        tf_rechercher.setText("");

        tableview.refresh();

    }

    @FXML
    private void combogenreAction(ActionEvent event) {
        String genre = combo_genre.getValue();
    }

    @FXML
    private void combosecteurAction(ActionEvent event) {
        String secteur = combo_secteur.getValue();
    }

//    @FXML
//    private void rechercherAction(InputMethodEvent event) {
//    }

    @FXML
    private void rechercherAction(ActionEvent event) {
        if (tf_rechercher.getText() != null) {
            String text = tf_rechercher.getText();
            text = text.toLowerCase();//conversion
            resultSearch(listInit, text);            
        }else {
            System.out.println("Entrer le mot à chercher");
            tableview.refresh();
            raz();
        }
    }

    private void resultSearch(ArrayList<Student> listInit, String text) {
        ArrayList<Student> searchList = new ArrayList<>();

        for (Student student : listInit) {
            String stud = student.toString().toLowerCase();//same conversino
            boolean test = stud.contains(text);
            if (test) {
                searchList.add(student);
            }
        }
        if(searchList.isEmpty()){
            System.out.println("Pas de correspondance pour : "+text);
        }
        showList(searchList);
    }
}
