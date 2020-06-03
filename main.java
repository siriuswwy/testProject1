import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class main {
	private final static int SUPPORT = 50; // 支持度阈值
    private final static String ITEM_SPLIT = " "; // 项之间的分隔符
    //将数据文件放入List--------------------------------------------------------------------------------------------------
	public static void readFile(ArrayList<String> dataList) {
        String pathname = "D:\\大三\\大三下\\数据挖掘\\Apriori实验数据\\T1014D50K.dat"; // 绝对路径
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                dataList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();}
        }
//计算所有频繁集----------------------------------------------------------------------------------------------------------
	 public static Map<String, Integer> getFC(ArrayList<String> dataList) {
	        Map<String, Integer> frequentCollectionMap = new HashMap<String, Integer>();// 所有的频繁集

	        frequentCollectionMap.putAll(getItem1FC(dataList));//将频繁1项集放入frequentCollectionMap

	        Map<String, Integer> itemkFcMap = new HashMap<String, Integer>();
	        itemkFcMap.putAll( frequentCollectionMap);//将频繁1项集放入itemkFcMap
	        while (itemkFcMap != null && itemkFcMap.size() != 0) {//键值对的数量
	            Map<String, Integer> candidateCollection = getCandidateCollection(itemkFcMap);

	            // 对候选集项进行累加计数
	            Set<String> ccKeySet = candidateCollection.keySet();
	            for (String trans : dataList) {
	                for (String candidate : ccKeySet) {
	                    boolean flag = false;// 用来判断交易中是否出现该候选项，如果出现，计数加1
	                    String[] transItems=trans.split(ITEM_SPLIT);
	                    String[] candidateItems = candidate.split(ITEM_SPLIT);
	                    int i=0;
	                    for(String candidateItem : candidateItems) {
	                    	for (String transItem:transItems) {
	                    		if(transItem.equals(candidateItem)) {
	                            	i++;
	                    			break;
	                    		}
	                    	}
	                    	if(i==candidateItems.length) {
	                    		flag=true;
	                    	}
	                   }
	                    if (flag) {
	                        Integer count = candidateCollection.get(candidate);
	                        candidateCollection.put(candidate, count + 1);
	                    }
	                }
	            }
	            itemkFcMap.clear();
	            int n=0;
	            int len=0;
	            for (String candidate : ccKeySet) { 
	                if (candidateCollection.get(candidate) >= SUPPORT) {
	                	String[] CFC=candidate.split(ITEM_SPLIT);
	                	len=CFC.length;
	                	n++;//计算各个项集的个数
	                    itemkFcMap.put(candidate, candidateCollection.get(candidate));
	                }
	            }
	            if(len!=0) {
		            System.out.println("频繁"+len+"项集有"+n+"个:");
		            System.out.println(itemkFcMap);
	            }
	            // 合并所有频繁集
	            frequentCollectionMap.putAll(itemkFcMap);
	        }

	        return frequentCollectionMap;
	    }

//根据前一个频繁集计算所有候选集---------------------------------------------------------------------------------------------
	 private static Map<String, Integer> getCandidateCollection(Map<String, Integer> itemkFcMap) {
	        Map<String, Integer> candidateCollection = new HashMap<String, Integer>();
	        Set<String> itemkSet1 = itemkFcMap.keySet();
	        Set<String> itemkSet2 = itemkFcMap.keySet();

	        for (String itemk1 : itemkSet1) {
	            for (String itemk2 : itemkSet2) {
	                // 进行连接
	                String[] tmp1 = itemk1.split(ITEM_SPLIT);
	                String[] tmp2 = itemk2.split(ITEM_SPLIT);
	                String c = "";
	                if (tmp1.length == 1) {
	                    if (tmp1[0].compareTo(tmp2[0]) < 0) {
	                        c = tmp1[0] + ITEM_SPLIT + tmp2[0] + ITEM_SPLIT;
	                    }
	                } else {
	                    boolean flag = true;
	                    for (int i = 0; i < tmp1.length - 1; i++) {
	                        if (!tmp1[i].equals(tmp2[i])) {
	                            flag = false;
	                            break;
	                        }
	                    }
	                    if (flag&& (tmp1[tmp1.length - 1].compareTo(tmp2[tmp2.length - 1]) < 0)) {
	                        c = itemk1 + tmp2[tmp2.length - 1] + ITEM_SPLIT;
	                    }
	                }
	                // 进行剪枝
	                boolean hasInfrequentSubSet = false;
	                if (!c.equals("")) {
	                    String[] tmpC = c.split(ITEM_SPLIT);
	                    for (int i = 0; i < tmpC.length; i++) {
	                        String subC = "";
	                        for (int j = 0; j < tmpC.length; j++) {
	                            if (i != j) {
	                                subC = subC + tmpC[j] + ITEM_SPLIT;
	                            }
	                        }
	                        if (itemkFcMap.get(subC) == null) {
	                            hasInfrequentSubSet = true;
	                            break;
	                        }
	                    }
	                } else {
	                    hasInfrequentSubSet = true;
	                }
	                if (!hasInfrequentSubSet) {
	                    candidateCollection.put(c, 0);
	                }
	            }
	        }
	        return candidateCollection;
	    }
	    
	//频繁1项的算法---------------------------------------------------------------------------------------------------------
	 private static Map<String, Integer> getItem1FC(ArrayList<String> dataList) {
	        Map<String, Integer> sItem1FcMap = new HashMap<String, Integer>();
	        Map<String, Integer> rItem1FcMap = new HashMap<String, Integer>();// 返回的频繁1项集

	        for (String trans :  dataList) {
	            String[] items = trans.split(ITEM_SPLIT);
	            for (String item : items) {
	                Integer count = sItem1FcMap.get(item + ITEM_SPLIT);
	                if (count == null) {
	                    sItem1FcMap.put(item + ITEM_SPLIT, 1);
	                } else {
	                    sItem1FcMap.put(item + ITEM_SPLIT, count + 1);
	                }
	            }
	        }
	        //System.out.println(sItem1FcMap);
	        Set<String> keySet = sItem1FcMap.keySet();
	        int n=0;
	        for (String key : keySet) {
	            Integer count = sItem1FcMap.get(key);
	            if (count >= SUPPORT) {
	                rItem1FcMap.put(key, count);
	                //System.out.print(key+"="+count+" , ");
	                n++;
	            }
	        }
	        System.out.println("频繁"+1+"项集有"+n+"个:");
            System.out.println(rItem1FcMap);
	        return rItem1FcMap;// 返回的频繁1项集
	    }
//	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> dataList = new ArrayList<String>();
		readFile(dataList);
		getFC(dataList);
	}
}
