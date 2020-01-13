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
import java.util.List;

public class MethodParser {
    private List<Method> methodList = new ArrayList<Method>();
    private String path;
    public MethodParser(String path){
        this.path = path;
    }
    public List<Method> parseMethod() throws IOException {
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
        return methodList;
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
            Method m = new Method(start,end,path,n.toString(ppc),n.getName().asString());
            methodList.add(m);
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
            Method m = new Method(start,end,path,c.toString(ppc),c.getName().asString());
            methodList.add(m);
            super.visit(c,arg);
        }
    }


}