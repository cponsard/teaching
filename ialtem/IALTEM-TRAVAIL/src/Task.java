import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

public class Task {
    
    String url;
    double score;
    boolean success;
    
    public Task(String url) {
        this.url=url;
    }
    
    public double performChecks() throws MalformedURLException, IOException {
        score=0;
        success=false;
        
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode node = cleaner.clean(new URL(url));
        
        // performing specific checks
        double scoreAltName=checkImgAltName(node);
        double scoreStruct=checkPageStruct(node);
        // other rules could be added...
        
        // computing score
        score=(scoreAltName+scoreStruct)/2;
        success=true;
        return score;
    }

    private double checkPageStruct(TagNode node) {
        PageStructVisitor visitor=new PageStructVisitor();
        node.traverse(visitor);
//      System.out.println(visitor.getResult());
        return visitor.getResult();
    }

    private double checkImgAltName(TagNode node) {
        ImgAltNodeVisitor visitor=new ImgAltNodeVisitor();
        node.traverse(visitor);
//      System.out.println(visitor.getResult());
        return visitor.getResult();
    }

    class ImgAltNodeVisitor implements TagNodeVisitor {
        int tot=0;
        double ok=0;
        
        public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
            if (htmlNode instanceof TagNode) {
                TagNode tag = (TagNode) htmlNode;
                String tagName = tag.getName();
                if ("img".equals(tagName)) {
                    tot++;
                    String alt = tag.getAttributeByName("alt");
                    if (alt != null) {
                        int l=alt.trim().length();
                        if (l>=50) ok+=1.0;
                        else if (l>=20) ok+= 0.5;
                        else if (l>=10) ok+=0.2;
                        else if (l>=5) ok+= 0.1;
                    }
                }
            }
            // tells visitor to continue traversing the DOM tree
            return true;
        }
        
        double getResult() {
            if (tot==0) return 1.0; // no image so no problems
            return ok/tot;          // weighted ratio
        }
    }

    class PageStructVisitor implements TagNodeVisitor {
        int ok=0;
        int prev=0;
        int tot=0;
        int hole=0;
        
        public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
            if (htmlNode instanceof TagNode) {
                TagNode tag = (TagNode) htmlNode;
                String tagName = tag.getName().toUpperCase();
                if ("H1".equals(tagName)) {
                    tot++; 
                    prev=1; 
                }
                if ("H2".equals(tagName)) { 
                    tot++;
                    if (prev<1) hole++;
                    prev=2; 
                }
                if ("H3".equals(tagName)) { 
                    tot++;
                    if (prev<2) hole++;
                    prev=3; 
                }
                if ("H4".equals(tagName)) { 
                    tot++;
                    if (prev<3) hole++;
                    prev=4; 
                }
            }
            // tells visitor to continue traversing the DOM tree
            return true;
        }
        
        double getResult() {
            if (tot==0) return 0; // no structure
            
            double res=(tot-hole*0.5)/tot;
            if (res<0) res=0;
            return res;
        }
    }
}