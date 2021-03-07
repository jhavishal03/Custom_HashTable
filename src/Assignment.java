
import java.util.*;
class HNode<K, V> {
     K key;
     V value;
     //reference fro next pointer
     HNode<K,V> next;
     HNode(K key,V value){
         this.key=key;
         this.value=value;
     }

}

// this represent all method
class HTable<K,V> implements  Iterable<K>
{
 private final float loadfactor=0.75f;
 private  int tableSize;
 private int size;
 ArrayList<HNode<K,V>> table;
 HTable(){
     table=new ArrayList<>();
     tableSize=11;
     size=0;
     for(int i=0;i<tableSize;i++){

         table.add(null);
     }
 }
private   int getIndexByHashing(K key){
     int code=key.hashCode();
     return code%tableSize;
}
private  void resizeTable(){
     int oldsize=table.size();
     int newsize=(oldsize<<1)+1;
     ArrayList<HNode<K,V>> temp=table;
     for(int i=0;i<newsize;i++){
         table.add(null);
     }
     for(HNode<K,V> it:temp){
          while(it!=null){
              insert(it.key,it.value);
              it=it.next;
          }
     }
}

//insert a node or value
 public synchronized void insert(K key,V value){
     if((1.0*size)/tableSize>loadfactor){
         resizeTable();
     }
     if(key==null || value==null)throw new RuntimeException("key or value cannot be null");
     int ind=getIndexByHashing(key);
     HNode<K,V> ptr=table.get(ind);
     HNode<K,V> prev=null;
     HNode<K,V> newnode=new HNode<K,V>(key, value);
     if(ptr==null){
         table.set(ind,newnode);
         size++;
         return;
     }
     else{
         while (ptr!=null){
             if(ptr.key.equals(key)){
                  ptr.value=value;
                  return;
             }
             prev=ptr;
             ptr=ptr.next;
         }
         if(ptr==null){
             prev.next=newnode;
             ptr=newnode;
             prev=ptr;
             ptr.next=null;
         }
     }
 }

// get value by key
     public V getValueByKey(K key) {
         int index = getIndexByHashing(key);

         HNode<K, V> head = table.get(index);
         while (head!=null){
             if(head.key.equals(key)){
                 return head.value;
             }
             head=head.next;
         }

         return null;
     }
   public  int getSize(){
     return size;
   }
     public synchronized V delete(K key){
        int ind=getIndexByHashing(key);
        HNode<K,V> head=table.get(ind);
        if(head==null)return null;
        if(head.key.equals(key)){
            V cvalue= head.value;;
            head=head.next;
            table.set(ind,head);
            size--;
            return  cvalue;
        }
        else{
            HNode<K,V> prev=null;
            while(head!=null){
                if(head.key.equals(key)){
                    prev.next=head.next;
                    size--;
                    return head.value;
                }
                prev=head;
                head=head.next;
            }
            size--;
            return null;
        }
     }

     public boolean contains(K key){
     if(key==null)throw new NullPointerException("Null key is not allowed");
     int ind=getIndexByHashing(key);
     HNode<K,V> head=table.get(ind);
     while(head!=null){
         if(head.key.equals(key)){
             return true;
         }
         head=head.next;
     }
     return false;
     }

    @Override
    public Iterator<K> iterator() {
        return new HTIterator();
 }

 class HTIterator implements Iterator<K> {
  private int currsize;
  private int currind;
     HNode<K,V> head;
    HTIterator(){
        currsize =0;
        currind=0;
        head=table.get(currind);
    }
     @Override
     public boolean hasNext() {
         return currsize !=size;
     }

     @Override
     public K next() {
      if(!hasNext())return null;
           while(head==null){
               currind++;
               head=table.get(currind);
           }
           currsize++;
           K key=head.key;
           head=head.next;
         return key;
     }

 }
}


/*
HTable has methods or properties->
insert(key,value);
getvaluebyKey(key); to get value if exist else null
delete(key) to remove a key with value is returned;
contains->
iterator-> to iterate through hashtable(next()return currentvalue and moves to next value
and hasnext() return whether more element is present or not;
size-> size of table
contains-> return whether value with key is accesible or not
 */
public class Assignment {
     public static void main(String[] args){
         HTable<String,Integer> obj=new HTable<>();
         obj.insert("abs",122);
         obj.insert("abcd",121);
         obj.insert("abc",1211);
         obj.insert("xyz",1);
         obj.insert("xyz",6171);
         System.out.println( obj.getValueByKey("xyz"));


       Iterator<String> it= obj.iterator();
       while (it.hasNext()){
           String s= it.next();
           System.out.println("key->"+s+ "\tvalue->" +obj.getValueByKey(s));
       }

//         obj.insert("abss",null);
//         System.out.println(obj.getSize());
//        System.out.println( obj.delete("abs"));
//        System.out.println(obj.contains("abs"));

     }
}


