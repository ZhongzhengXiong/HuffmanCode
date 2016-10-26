import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;

/**
 * Created by XiongZZ on 2016-10-17.
 */
public class FileUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        final File[] file = {null, null};


        FileChooser fileChooser1 = new FileChooser();
        fileChooser1.setTitle("Open Source File");

        DirectoryChooser fileChooser2 = new DirectoryChooser();
        fileChooser2.setTitle("Destination");

        DirectoryChooser fileChooser3 = new DirectoryChooser();
        fileChooser3.setTitle("Open Source Folder");

        final Button sourceButton = new Button("File");
        final Button destButton = new Button("Choose");
        final Button folderButton = new Button("Folder");
        Button compress = new Button("Compress");
        Button decompress = new Button("Decompress");

        sourceButton.setMinWidth(60);
        folderButton.setMinWidth(60);
        destButton.setMinWidth(130);

        HBox btHbox = new HBox();
        btHbox.getChildren().add(sourceButton);
        btHbox.getChildren().add(folderButton);

        TextField textField1 = new TextField();
        textField1.setPromptText("SourceFolder");
        TextField textField2 = new TextField();
        textField2.setPromptText("Destination");
        textField1.setEditable(false);
        textField2.setEditable(false);
        textField1.setFocusTraversable(false);
        textField2.setFocusTraversable(false);
        textField1.setMinSize(500, 25);
        textField2.setMinSize(500, 25);


        pane.add(textField1, 0, 0);
        pane.add(btHbox, 1, 0);
        pane.add(textField2, 0, 1);
        pane.add(destButton, 1, 1);

        compress.setAlignment(Pos.CENTER);
        decompress.setAlignment(Pos.CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().add(pane);
        vBox.getChildren().add(compress);
        vBox.getChildren().add(decompress);
        vBox.setSpacing(25);
        vBox.setAlignment(Pos.CENTER);
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        vBox.getChildren().add(textArea);


        sourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                file[0] = fileChooser1.showOpenDialog(primaryStage);
                if (file[0] != null) {
                    textField1.setText(file[0].getAbsolutePath());
                }
            }
        });
        folderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                file[0] = fileChooser3.showDialog(primaryStage);
                if (file[0] != null) {
                    textField1.setText(file[0].getAbsolutePath());
                }
            }
        });
        destButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                file[1] = fileChooser2.showDialog(primaryStage);
                if (file[1] != null) {
                    textField2.setText(file[1].getAbsolutePath());
                }
            }
        });

        Stage prompt1 = new Stage();
        prompt1.setTitle("Warning");
        prompt1.setScene(new Scene(new TextArea("Source File Is Null!!!")));
        Stage prompt2 = new Stage();
        prompt2.setTitle("Warning");
        prompt2.setScene(new Scene(new TextArea("Destination File Is Null!!!")));
        Stage prompt3 = new Stage();
        prompt3.setTitle("Warning");
        prompt3.setScene(new Scene(new TextArea("Wrong File Type !!!")));

        compress.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (file[0] == null)
                    prompt1.show();
                else if (file[1] == null)
                    prompt2.show();
                else {
                    long start, end = 0;
                    start = System.currentTimeMillis();
                    if (file[0].isDirectory()) {
                        start = System.currentTimeMillis();
                        File infile = file[0];
                        DirectoryTreeNode root = new DirectoryTreeNode(0, infile.getName());
                        CompressDirectory.readFile(infile, root);
                        CompressDirectory.getBytenum(root);
                        File outfile = new File(file[1].getAbsolutePath() + "\\" + file[0].getName() + ".haff");
                        try {
                            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outfile));
                            //write head
                            CompressDirectory.WriteHead(root, outputStream);
                            //write file
                            CompressDirectory.writeFile(infile, outputStream);
                            outputStream.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        end = System.currentTimeMillis();
                        System.out.println("compress folder time: " + (end - start) + "ms");
                    } else {
                        File infile = file[0];
                        File outfile = new File(file[1].getAbsolutePath() + "\\" + getFileNameNoEx(file[0].getName()) + ".haff");
                        if (!outfile.exists()) {
                            try {
                                outfile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outfile));
                            Compress.CompressSingleFile(infile, outputStream, false);
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        end = System.currentTimeMillis();
                    }
                    textArea.setText("Compression finished. Total time: " + (end - start) + "ms");
                }
            }
        });
        decompress.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (file[0] == null) {
                    prompt1.show();
                }else if (file[1] == null){
                    prompt2.show();
                }
                else {
                    if (file[0].isDirectory() || !getExtensionName(file[0].getName()).equals(new String("haff"))) {
                        prompt3.show();
                    } else {
                        long start = System.currentTimeMillis();
                        File input = file[0];
                        BufferedInputStream inputStream = null;
                        try {
                            inputStream = new BufferedInputStream(new FileInputStream(input));
                            //读出文件名称
                            int firstByte = inputStream.read();
                            if (firstByte != 0) {
                                byte[] b = new byte[firstByte];
                                inputStream.read(b);
                                String name = new String(b);
                                File output = new File(file[1].getAbsolutePath() + "\\" + name);
                                Decompress.Decompress(inputStream, output);
                                inputStream.close();
                            } else {
                                //解析头文件
                                //获取根节点
                                int type = firstByte;
                                int byteNum1 = inputStream.read();
                                int byteNum2 = inputStream.read();
                                int byteNum3 = inputStream.read();
                                int byteNum4 = inputStream.read();
                                int byteNum = Integer.rotateLeft(byteNum1, 24) + Integer.rotateLeft(byteNum2, 16) + Integer.rotateLeft(byteNum3, 8) + byteNum4;
                                int namelength = inputStream.read();
                                byte[] name = new byte[namelength];
                                inputStream.read(name);
                                DirectoryTreeNode root = new DirectoryTreeNode(0, new String(name));
                                root.setByteNum(byteNum);
                                //建立文件树
                                DecompressDirectory.ParseHead(inputStream, root);
                                //建立文件系统
                                String path = file[1].getAbsolutePath();
                                DecompressDirectory.BuildFile(root, path);
                                File output = new File(path + "\\" + root.getName());
                                DecompressDirectory.parseFile(inputStream, output);
                                inputStream.close();
                                long end = System.currentTimeMillis();
                                System.out.println("decompress folder time " + (end - start) + "ms");
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        long end = System.currentTimeMillis();
                        textArea.setText("Decompression end Total time: " + (end - start) + "ms");
                    }
                }
            }
        });


        primaryStage.setTitle("HuffmanCompress");
        Scene scene = new Scene(vBox);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
}
