package Master;
import org.json.*;
import java.io.*;
import org.apache.commons.io.FileUtils;
class Pois
{
    static JSONObject[]POIS = new JSONObject[1692];
    static double[] latitudes = new double[1692];
    static double[] longidudes= new double[1692];
    static String[] category= new String[1692];
    static String[] name= new String[1692];
    void fillPOISarray()
    {
        try
        {
            File file = new File("POIs.json");
            String content = FileUtils.readFileToString(file, "utf-8");
            JSONObject jsonObject = new JSONObject(content);
            for(int i=0; i<POIS.length;i++)
            {
                POIS[i]= jsonObject.getJSONObject(Integer.toString(i));
                latitudes[i] = Double.parseDouble(POIS[i].get("latidude").toString());
                longidudes[i] = Double.parseDouble(POIS[i].get("longitude").toString());
                category[i] = POIS[i].get("POI_category_id").toString();
                name[i] = POIS[i].get("POI_name").toString();
            }
        }catch (IOException e){e.printStackTrace();}
    }

}
