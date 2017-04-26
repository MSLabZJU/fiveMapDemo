package com.tristan.astar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import android.graphics.Point;


//ע�⣬���������������,map_column��Ӧ����x��,map_row��Ӧ����y��
public class GraphForAstar {
	private int map_row, map_column;   	//��ͼ��С
	private int numOfPoints;			//�������
	private Barrier barrier;            //�ϰ���
	private Point src;               	//���
	private Point dst; 					//�յ�
	private ArrayList<Point> finalPath = new ArrayList<Point>(); //�������ɵ�·��
	
	//����0/1�����ʾ���±�Ϊÿ��Point��Ψһid
	private boolean[] open;   				//OPEN��,0������,1������
	private boolean[] close;				//CLOSE��,0������,1������
//	private byte[] block;				//�ϰ��Ｏ
	
	//�洢ÿ�����f��g��h
	private float[] f;
	private float[] g;
	private float[] h;
	
	private ArrayList<CountHelper> indexForOpen = new ArrayList<CountHelper>();
	private ArrayList<CountHelper> indexForAll = new ArrayList<CountHelper>();
	
	//������
	public GraphForAstar(int map_row, int map_column, Barrier barrier, Point src, Point dst){
		this.map_row = map_row;
		this.map_column = map_column;
		this.numOfPoints = map_row * map_column;
		
		this.barrier = barrier;
		this.src = new Point(src);
		this.dst = new Point(dst);
		
		//��ʼ������
		open = new boolean[numOfPoints];
		Arrays.fill(open, false);
		close = new boolean[numOfPoints];
		Arrays.fill(close, false);
		f = new float[numOfPoints];
		Arrays.fill(f, Float.MAX_VALUE);
		g = new float[numOfPoints];
		Arrays.fill(g, Float.MAX_VALUE);
		h = new float[numOfPoints];
		Arrays.fill(h, Float.MAX_VALUE);
		
		//ĳ��λ�ô����ϰ�ʱ������close����������1
		for (Point p : barrier.getBarrierPoint()) {
			int id = getID(p);
			close[id] = true;
		}
		
	}
	
	//���������·��
	public void calculatePath(){
		int src_id = getID(src);
		int dst_id = getID(dst);
		int next_id;                    //��һ�����id
		
		//������Id���뵽close������������f\g\h
		close[src_id] = true;
		g[src_id] = 0;
		h[src_id] = getCost_h(src);
		f[src_id] = getCost_f(src_id);
		
		boolean isDstInOpen = false;
		next_id = src_id;         //��ʼ��������ÿ�ζ����õ�f��С�ĵ��id
		
		//��ʼ����ѭ����������Χ�ĵ�
		while(!isDstInOpen){
			handleNeighbors(getPointFromID(next_id));
			next_id = nextFmin();
			isDstInOpen = isDstinOpenNow();
		}
		
		//��ȫ�������꣬dst��id����open[]��
		//�ҵ�·�����������finalPath��
		finalPath.add(dst);
		int loopIndex = dst_id;        //ѭ������
		while(loopIndex != src_id){
			//������������·����ѭ��
			for (CountHelper helper : indexForAll) {
				if(helper.getIndex()== loopIndex){
					loopIndex = helper.getFatherIndex();
					finalPath.add(getPointFromID(loopIndex));
				}
			}
		}
		finalPath.add(src);
	}
	
	public ArrayList<Point> getFinalPath(){
		return this.finalPath;
	}
	
	//��ʱ�����յ㲻��ȡ�߽�ֵ
	//��������һ������Χ��8����
	public void handleNeighbors(Point center){
		int center_id = getID(center);
		//�������ھ�
		for (int index_x=center.x-1; index_x<center.x+2; index_x++){
			for (int index_y=center.y-1; index_y<center.y+2; index_y++){
				
				int neighbor_id = index_y*map_column+index_x;
				Point neighborPoint  = getPointFromID(neighbor_id);
				//������м�ĵ������
				if (neighbor_id == center_id) continue;
				//�����close�еĵ�������
				if (close[neighbor_id] == true) {
					continue;
				}
				//�������open�������
				if (!open[neighbor_id]){
					open[neighbor_id] = true;
					indexForOpen.add(new CountHelper(neighbor_id,center_id));
					indexForAll.add(new CountHelper(neighbor_id,center_id));
					g[neighbor_id] = getCost_g(center, neighborPoint)+g[center_id];
					h[neighbor_id] = getCost_h(neighborPoint);
					f[neighbor_id] = getCost_f(neighbor_id);
				}
				//����Ѿ���open�У���Ƚ�gֵ��С�Ƿ��С�������С������£��Ҹı丸�ڵ�
				if (open[neighbor_id]){
					float temp_g = g[center_id]+getCost_g(center, neighborPoint);
					if (g[neighbor_id]>temp_g){
						g[neighbor_id] = temp_g;
						f[neighbor_id] = getCost_f(neighbor_id);
						for (CountHelper helper : indexForAll) {
							if(helper.getIndex() == neighbor_id){
								helper.resetFatherIndex(center_id);
							}
						}
					}
				}
				
//				System.out.println(neighborPoint+" "+g[neighbor_id]+" "+h[neighbor_id]+" "+f[neighbor_id]);
			}
		}
		
	}
	
	//��open���ҵ�f��С�ĵ���Ϊ��һ���㣬ͬʱ����������close������open��ɾ��
	public int nextFmin(){
		//temp_index���������С��f����Ӧ���Ǹ�Point���±�,��ʱ�ȱ���Ϊ��һ�����±�
		int count = indexForOpen.size();
		int min_index = indexForOpen.get(count-1).getIndex();
		int indexInArray = count-1;
		float min_f = f[min_index];
		
		//����ʼ����
		for (int i=count-1; i>=0;i--){
			int index = indexForOpen.get(i).getIndex();
			if(f[index]<min_f){
				min_f = f[index];
				min_index = index;
				indexInArray = i;
			}
		}
		open[min_index] = false;    //��open��ɾ��
		close[min_index] = true;	//���뵽close��
		indexForOpen.remove(indexInArray);
		return min_index;
	}
	
	
	//���յ�����Ӧ��id�Ƿ��Ѿ���������,һ���ҵ��ˣ�����true�����򷵻�false
	public boolean isDstinOpenNow(){
		for (CountHelper helper : indexForAll) {
			int index = helper.getIndex();
			if (index == getID(dst)) return true;  
		}
		return false;
	}
	
	//���һ�����Ӧ��Ψһ��id
	public int getID(Point p){
		int id = p.y*map_column+p.x;
		return id;
	}
	
	//��ȡid��Ӧ��Ψһ�ĵ�
	public Point getPointFromID(int id){
		int x = id%map_column;
		int y = id/map_column;
		return new Point(x,y);
	}
	
	//����֮���Gֵ
	public float getCost_g(Point point_a,Point point_b){
		if(Math.abs(point_a.x-point_b.x)==1 && Math.abs(point_a.y-point_b.y)==1){
			return (float) 1.414;
		}else {
			float distance = Math.abs(point_a.x-point_b.x)+Math.abs(point_a.y-point_b.y);
			return distance;
		}
	}
	
	//��ǰ�㵽�յ������ֵ
	public float getCost_h(Point point){
		float cost_h =  Math.abs(point.x-dst.x)+Math.abs(point.y-dst.y);
		return cost_h;
	}
	
	public float getCost_f(int id){
		float cost_f = g[id] + h[id];
		return cost_f;
	}
	
}
