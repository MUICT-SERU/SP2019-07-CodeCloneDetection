import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JavaMethodParser {
    private List<Method> methodList = new ArrayList<Method>();
    private HashMap<String,Method> methodHashMap= new HashMap<>();
    private String path;

    private String fileName;
    public JavaMethodParser(String path){
        this.path = path;
        //fileName = path.split("/")[path.split("/").length-1].split(".")[0];
        String[] s = path.split("/");
        String file = s[s.length-1];
        String[] fn = file.split("\\.");
        fileName = fn[0];
    }
    public HashMap<String,Method> parseMethod() throws IOException {
        try{
            FileInputStream in = new FileInputStream(this.path);
            CompilationUnit cu;
            try {
                cu = StaticJavaParser.parse(in);
                NodeList<TypeDeclaration<?>> types = cu.getTypes();
                for(TypeDeclaration type : types){
                    if(type instanceof ClassOrInterfaceDeclaration){
                        ClassOrInterfaceDeclaration classDec = (ClassOrInterfaceDeclaration) type;
                        String className = classDec.getName().asString();
                    }
                }
                new MethodVisitor().visit(cu,null);
                new ConstructorVisitor().visit(cu,null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return methodHashMap;
    }

    private class MethodVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(MethodDeclaration n, Object arg){
            PrettyPrinterConfiguration ppc = new PrettyPrinterConfiguration();
            ppc.setPrintComments(false);
            ppc.setPrintJavadoc(false);
            int start = -1;
            int end = -1;
            if(n.getBegin().isPresent()){
                start = n.getBegin().get().line;
            }
            if(n.getEnd().isPresent()){
                end = n.getEnd().get().line;
            }
            Method m = null;
            MultiKey mk = null;
            try {
                m = new Method(start,end,path,fileName,n.toString(ppc),n.getName().asString(),n.getTypeAsString());
            } catch (Exception e) {
                e.printStackTrace();
            }
//            methodList.add(m);
            methodHashMap.put(fileName+start+end,m);
            super.visit(n,arg);
        }
    }

    private class ConstructorVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ConstructorDeclaration c, Object arg){
            PrettyPrinterConfiguration ppc = new PrettyPrinterConfiguration();
            ppc.setPrintComments(false);
            ppc.setPrintJavadoc(false);
            int start = -1;
            int end = -1;
            if(c.getBegin().isPresent()){
                start = c.getBegin().get().line;
            }
            if(c.getEnd().isPresent()){
                end = c.getEnd().get().line;
            }
            Method m = null;
            MultiKey mk = null;
            try {
                m = new Method(start,end,path,fileName,c.toString(ppc),c.getName().asString(),"Constructor");
                mk = new MultiKey(fileName,start,end);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            methodList.add(m);
            methodHashMap.put(fileName+start+end,m);
            super.visit(c,arg);
        }
    }


}
