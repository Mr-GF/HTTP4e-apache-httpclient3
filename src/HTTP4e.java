import java.util.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

/**
 * This is a sample Java HTTP Rest client generated from HTTP4e (http://www.ywebb.com).
 * It is based on Apache HttpClient 4.x
 */
public class HTTP4e {

    /**
     * The main method contains your HTTP4e data. You should modify the client here.
     */
    public static void main(String[] args) {
        HttpRunner.Bean bean = new HttpRunner.Bean();
        bean.method = "POST";
        bean.url = "http://www.yahoo.com";
        bean.body = "qwe=1&fff=2";
        bean.addHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpRunner httpRunner = new HttpRunner();
        HttpRunner.ResponseReader responseReader = new HttpRunner.ResponseReader() {
            public void read(HttpMethod httpMethod) {
                try {
                    System.out.println(httpMethod.getStatusLine());
                    System.out.println("\n" + httpMethod.getResponseBodyAsString());
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        };
        httpRunner.execute(bean, responseReader);
    }
}

/**
 * This class encapsulates HTTP complexities and executes a HTTP call
 */
class HttpRunner {
    
    private Bean bean;
    private HttpMethod httpMethod = null;    

    public void execute(Bean tBean, ResponseReader responseReader) {
        this.bean = tBean;
        
        try {
            if ("GET".equals(bean.method)) {
                httpMethod = new GetMethod(bean.url);
                addHeaders(httpMethod);
                
            } else if ("POST".equals(bean.method)) {
                httpMethod = new PostMethod(bean.url);
                addHeaders(httpMethod);
                addParams((PostMethod) httpMethod);
                RequestEntity re = new StringRequestEntity(bean.body, null, "UTF8");
                ((PostMethod) httpMethod).setRequestEntity(re);
                
            } else if ("HEAD".equals(bean.method)) {
                httpMethod = new HeadMethod(bean.url);
                addHeaders(httpMethod);
                
            } else if ("PUT".equals(bean.method)) {
                httpMethod = new PutMethod(bean.url);
                addHeaders(httpMethod);
                
                RequestEntity re = new StringRequestEntity(bean.body, null, "UTF8");
                ((PutMethod) httpMethod).setRequestEntity(re);
                
            } else if ("DELETE".equals(bean.method)) {
                httpMethod = new DeleteMethod(bean.url);
                addHeaders(httpMethod);
                
            } else if ("TRACE".equals(bean.method)) {
                httpMethod = new TraceMethod(bean.url);
                addHeaders(httpMethod);
                
            } else if ("OPTIONS".equals(bean.method)) {
                httpMethod = new OptionsMethod(bean.url);
                addHeaders(httpMethod);
                
            } else {
                throw new RuntimeException("Method '" + bean.method + "' not implemented.");
            }
            
            doExecute(httpMethod, responseReader);

        } catch (java.io.IOException e) {
            abort();
            e.printStackTrace();
            
        } catch (IllegalArgumentException e) {
            abort();
            e.printStackTrace();
            
        } catch (Exception e) {
            abort();
            e.printStackTrace();
        }        
    }    
    
    /**
     * Populating PostMethod with parameters
     */
    private void addParams(PostMethod postMethod) {
        for (String  key : bean.parameters.keySet()) {
            Collection<String> values = (Collection<String>) bean.parameters.get(key);
            StringBuilder sb = new StringBuilder();
            int cnt = 0;
            for (String val : values) {
                if (cnt != 0) {
                    sb.append(",");
                }
                sb.append(val);
                cnt++;
            }
            postMethod.setParameter(key, sb.toString());
        }
    }

    /**
     * Populating HttpMethod with headers
     */
    private void addHeaders(HttpMethod httpMethod) {
        for (String key : bean.headers.keySet()) {
           Collection<String> values = (Collection<String>) bean.headers.get(key);
            StringBuilder sb = new StringBuilder();
            int cnt = 0;
            for (String val : values) {
                if (cnt != 0) {
                    sb.append(",");
                }
                sb.append(val);
                cnt++;
            }
            httpMethod.addRequestHeader(key, sb.toString());
        }
    }

    /**
     * Executing HttpMethod.
     */
    private void doExecute(HttpMethod httpMethod, ResponseReader responseReader) {
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(httpMethod);
            
            responseReader.read(httpMethod);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
            
        } finally {
            if(httpMethod != null) httpMethod.releaseConnection();
        }
    }    

    /**
     * Canceling HttpMethod execution.
     */
    public void abort() {
        if (httpMethod == null) {
            return;
        }
        try {
            httpMethod.abort();
            httpMethod = null;
        } catch (Exception giveup) {}
    }
    
   /**
    * This is a helper class holding HTTP packet data.
    */
    public static class Bean {        
        String method = "GET";
        String url = "";
        String body = "";
        private Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>();
        private Map<String, Collection<String>> parameters = new HashMap<String, Collection<String>>();
        
        public void addHeader(String key, String value){
           List<String> valuesList = (List<String>)headers.get(key);
            if(valuesList == null){
                valuesList = new ArrayList<String>();                
            }
            valuesList.add(value);
            headers.put(key, valuesList);
        }
        
        public void addParam(String key, String value){
           Collection<String> valuesList = (Collection<String>)parameters.get(key);
            if(valuesList == null){
                valuesList = new ArrayList<String>();                
            }
            valuesList.add(value);
            parameters.put(key, valuesList);
        }
        
        public String toString() {
            return "{method=" + method + ",url=" + url + 
                   ",headers=" + headers + ",parameters=" + 
                   parameters + "}";
        }
    }
    
   /**
    * This interface is being hooked to the execution template method and  
    * it is being invoked on response read.
    */
    public static interface ResponseReader {   
         
        void read(HttpMethod httpMethod);
    }
        
}
