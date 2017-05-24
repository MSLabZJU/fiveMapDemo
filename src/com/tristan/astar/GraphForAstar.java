package com.tristan.astar;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Point;

/**
 * 注意，在我这个程序里面,map_column对应的是x轴,map_row对应的是y轴
 * @author TristanHuang
 * 2017-5-23 上午11:49:08
 */

public class GraphForAstar {
	private int map_row, map_column;   	//地图大小
	private int numOfPoints;			//点的总数
	private Barrier barrier;            //障碍物
	private Point src;               	//起点
	private Point dst; 					//终点
	private ArrayList<Point> finalPath = new ArrayList<Point>(); //最终生成的路径
	
	//采用0/1数组表示，下标为每个Point的唯一id
	private boolean[] open;   				//OPEN集,0代表不在,1代表在
	private boolean[] close;				//CLOSE集,0代表不在,1代表在
//	private byte[] block;				//障碍物集
	
	//存储每个点的f、g、h
	private float[] f;
	private float[] g;
	private float[] h;
	
	private ArrayList<CountHelper> indexForOpen = new ArrayList<CountHelper>();
	private ArrayList<CountHelper> indexForAll = new ArrayList<CountHelper>();
	
	//构造器
	public GraphForAstar(int map_row, int map_column, Barrier barrier, Point src, Point dst){
		this.map_row = map_row;
		this.map_column = map_column;
		this.numOfPoints = map_row * map_column;
		
		this.barrier = barrier;
		this.src = new Point(src);
		this.dst = new Point(dst);
		
		//初始化数组
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
		
		//某个位置存在障碍时，就在close集中让它置1
		for (Point p : barrier.getBarrierPoint()) {
			int id = getID(p);
			close[id] = true;
		}
		
	}
	
	//计算出最后的路径
	public void calculatePath(){
		int src_id = getID(src);
		int dst_id = getID(dst);
		int next_id;                    //下一个点的id
		
		//将起点的Id放入到close，并计算起点的f\g\h
		close[src_id] = true;
		g[src_id] = 0;
		h[src_id] = getCost_h(src);
		f[src_id] = getCost_f(src_id);
		
		boolean isDstInOpen = false;
		next_id = src_id;         //初始化，后面每次都是拿的f最小的点的id
		
		//开始进入循环，处理周围的点
		while(!isDstInOpen){
			handleNeighbors(getPointFromID(next_id));
			next_id = nextFmin();
			isDstInOpen = isDstinOpenNow();
		}
		
		//已全部搜索完，dst的id已在open[]中
		//找到路径，逆序存在finalPath中
		finalPath.add(dst);
		int loopIndex = dst_id;        //循环因子
		while(loopIndex != src_id){
			//进入搜索最终路径的循环
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
	
	//暂时起点和终点不能取边界值
	//用来处理一个点周围的8个点
	public void handleNeighbors(Point center){
		int center_id = getID(center);
		//遍历八邻居
		for (int index_x=center.x-1; index_x<center.x+2; index_x++){
			for (int index_y=center.y-1; index_y<center.y+2; index_y++){
				
				int neighbor_id = index_y*map_column+index_x;
				Point neighborPoint  = getPointFromID(neighbor_id);
				//如果是中间的点就跳过
				if (neighbor_id == center_id) continue;
				//如果是close中的点则跳过
				if (close[neighbor_id] == true) {
					continue;
				}
				//如果不在open中则加入
				if (!open[neighbor_id]){
					open[neighbor_id] = true;
					indexForOpen.add(new CountHelper(neighbor_id,center_id));
					indexForAll.add(new CountHelper(neighbor_id,center_id));
					g[neighbor_id] = getCost_g(center, neighborPoint)+g[center_id];
					h[neighbor_id] = getCost_h(neighborPoint);
					f[neighbor_id] = getCost_f(neighbor_id);
				}
				//如果已经在open中，则比较g值大小是否更小，如果更小，则更新，且改变父节点
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
	
	//在open中找到f最小的点作为下一个点，同时将这个点放入close，并在open中删除
	public int nextFmin(){
		//temp_index保存的是最小的f所对应的那个Point的下标,暂时先保存为第一个的下标
		int count = indexForOpen.size();
		int min_index = indexForOpen.get(count-1).getIndex();
		int indexInArray = count-1;
		float min_f = f[min_index];
		
		//逆序开始遍历
		for (int i=count-1; i>=0;i--){
			int index = indexForOpen.get(i).getIndex();
			if(f[index]<min_f){
				min_f = f[index];
				min_index = index;
				indexInArray = i;
			}
		}
		open[min_index] = false;    //从open中删除
		close[min_index] = true;	//加入到close中
		indexForOpen.remove(indexInArray);
		return min_index;
	}
	
	
	//看终点所对应的id是否已经搜索到了,一旦找到了，返回true，否则返回false
	public boolean isDstinOpenNow(){
		for (CountHelper helper : indexForAll) {
			int index = helper.getIndex();
			if (index == getID(dst)) return true;  
		}
		return false;
	}
	
	//获得一个点对应的唯一的id
	public int getID(Point p){
		int id = p.y*map_column+p.x;
		return id;
	}
	
	//获取id对应的唯一的点
	public Point getPointFromID(int id){
		int x = id%map_column;
		int y = id/map_column;
		return new Point(x,y);
	}
	
	//两点之间的G值
	public float getCost_g(Point point_a,Point point_b){
		if(Math.abs(point_a.x-point_b.x)==1 && Math.abs(point_a.y-point_b.y)==1){
			return (float) 1.414;
		}else {
			float distance = Math.abs(point_a.x-point_b.x)+Math.abs(point_a.y-point_b.y);
			return distance;
		}
	}
	
	//当前点到终点的启发值
	public float getCost_h(Point point){
		float cost_h =  Math.abs(point.x-dst.x)+Math.abs(point.y-dst.y);
		return cost_h;
	}
	
	public float getCost_f(int id){
		float cost_f = g[id] + h[id];
		return cost_f;
	}
	
}
