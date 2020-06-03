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
	private final static int SUPPORT = 50; // ֧�ֶ���ֵ
    private final static String ITEM_SPLIT = " "; // ��֮��ķָ���
    //�������ļ�����List--------------------------------------------------------------------------------------------------
	public static void readFile(ArrayList<String> dataList) {
        String pathname = "D:\\����\\������\\�����ھ�\\Aprioriʵ������\\T1014D50K.dat"; // ����·��
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // ����һ�����������ļ�����ת�ɼ�����ܶ���������
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                // һ�ζ���һ������
                dataList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();}
        }
//��������Ƶ����----------------------------------------------------------------------------------------------------------
	 public static Map<String, Integer> getFC(ArrayList<String> dataList) {
	        Map<String, Integer> frequentCollectionMap = new HashMap<String, Integer>();// ���е�Ƶ����

	        frequentCollectionMap.putAll(getItem1FC(dataList));//��Ƶ��1�����frequentCollectionMap

	        Map<String, Integer> itemkFcMap = new HashMap<String, Integer>();
	        itemkFcMap.putAll( frequentCollectionMap);//��Ƶ��1�����itemkFcMap
	        while (itemkFcMap != null && itemkFcMap.size() != 0) {//��ֵ�Ե�����
	            Map<String, Integer> candidateCollection = getCandidateCollection(itemkFcMap);

	            // �Ժ�ѡ��������ۼӼ���
	            Set<String> ccKeySet = candidateCollection.keySet();
	            for (String trans : dataList) {
	                for (String candidate : ccKeySet) {
	                    boolean flag = false;// �����жϽ������Ƿ���ָú�ѡ�������֣�������1
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
	                	n++;//���������ĸ���
	                    itemkFcMap.put(candidate, candidateCollection.get(candidate));
	                }
	            }
	            if(len!=0) {
		            System.out.println("Ƶ��"+len+"���"+n+"��:");
		            System.out.println(itemkFcMap);
	            }
	            // �ϲ�����Ƶ����
	            frequentCollectionMap.putAll(itemkFcMap);
	        }

	        return frequentCollectionMap;
	    }

//����ǰһ��Ƶ�����������к�ѡ��---------------------------------------------------------------------------------------------
	 private static Map<String, Integer> getCandidateCollection(Map<String, Integer> itemkFcMap) {
	        Map<String, Integer> candidateCollection = new HashMap<String, Integer>();
	        Set<String> itemkSet1 = itemkFcMap.keySet();
	        Set<String> itemkSet2 = itemkFcMap.keySet();

	        for (String itemk1 : itemkSet1) {
	            for (String itemk2 : itemkSet2) {
	                // ��������
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
	                // ���м�֦
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
	    
	//Ƶ��1����㷨---------------------------------------------------------------------------------------------------------
	 private static Map<String, Integer> getItem1FC(ArrayList<String> dataList) {
	        Map<String, Integer> sItem1FcMap = new HashMap<String, Integer>();
	        Map<String, Integer> rItem1FcMap = new HashMap<String, Integer>();// ���ص�Ƶ��1�

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
	        System.out.println("Ƶ��"+1+"���"+n+"��:");
            System.out.println(rItem1FcMap);
	        return rItem1FcMap;// ���ص�Ƶ��1�
	    }
//	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> dataList = new ArrayList<String>();
		readFile(dataList);
		getFC(dataList);
	}
}
