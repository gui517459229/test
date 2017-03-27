package com.shishike.calm.testCases.fastfood;

import org.junit.Test;

import com.shishike.calm.action.dinner.dinnerAction;
import com.shishike.calm.action.fastfood.fastfoodAction;
import com.shishike.calm.data.id.ID;

import com.shishike.calm.data.id.fastFoodId;
import com.shishike.calm.data.testdata.Constants;
import com.shishike.calm.testCases.FastFoodCaseTemp;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.Assert;

/***
 * 
 * @author fuli
 *
 */
public class OrderDish extends FastFoodCaseTemp {

	public OrderDish() throws ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * 称重、非变价、规格、加入类别、做法、口味、加料、商品、整单备注、单菜品备注-点单-购物车
	 */
	@Test
	public void test001OrderDish() throws Exception {
		// 未点菜验证购物车底栏显示
		assertEquals("Step1:购物车底栏未显示 “弹出收钱箱”", Constants.POPUPCASHBOX, solo.getTextStr(fastFoodId.POPUPCASHBOX));

		// 选择“甜品”中类
		action.selectCommodityClassification(Constants.SWEET);
		String[] dishNamePrice = action.getDishNamePrice(0);
		// 选择“蛋挞”
		action.selectDish(0);
		action.inputWeight(Constants.DISH_WEIGHT1);
		assertEquals("Step2:数量不一致", Constants.DISH_WEIGHT1, solo.getTextStr(ID.DISH_COUNT_EDIT));
		// 选择规格
		action.selectSpec(1);
		assertTrue("Step3:规格中没有选中。", action.isSelectedSpec(1));

		action.selectSpec(0);
		action.inputWeight(Constants.DISH_WEIGHT2);

		assertTrue("Step4:规格大没有选中。", action.isSelectedSpec(0));
		// 选择单菜品备注-酸甜
		solo.clickOnView(ID.COMMON_DEMO_BUTTON);
		// 获取选择的单菜品备注
		action.selectCommonDemo(1, 2);
		solo.clickOnView(ID.COMMON_DEMO_BUTTON);
		String demoD = solo.getTextStr(ID.DEMO_EDIT);

		int index1[] = action.getCategoryIndexScope(); 
		int index2[] = action.getPracticeIndexScope();
		int index3[] = action.getFlavorIndexScope();
		// 设置菜品的 类别、做法、口味
		action.setCommodityPriceDishCPF_Weight1(index1[0], 0, index2[0], 2, index3[0], 1);
		String cpf[] = action.getSelectedCategoryPracticeFlavor();
		String cpfPrice[] = action.getSelectedCategoryPracticeFlavorPrice();
		// 获取选择的类别、做法、口味名称 addTexts【0】类别 addTexts【1】 做法 addTexts【2】口味
		String[] addTexts = action.getSelectedCategoryPracticeFlavor();
		// 获取选择的类别、做法、口味的价格
		String[] addTextsPrice = action.getSelectedCategoryPracticeFlavorPrice();
		// 计算选择的 类别、做法、口味的 价格（原菜品数量*类别单价 /做法单价/口味单价）
		String[] addPrice = fastfoodaction.getFastAddPrice(Constants.DISH_WEIGHT2, addTextsPrice[0], addTextsPrice[1],
				addTextsPrice[2]);

		// 选择加料
		action.selectFeed(1, 0);
		action.addFeed(1, 0, 2);
		action.selectFeed(1, 1);
		action.addFeed(1, 1, 3);
		// 计算加料总金额

		String fePrice[] = action.getSelectedFeedPrice();
		String fe = action.getSelectedFeed();
		float feedPrice = 0;
		for (int i = 0; i < fePrice.length; i++) {
			feedPrice += Float.parseFloat(fePrice[i].substring(2));
		}
		String dFeedPrice = "￥" + String.valueOf(feedPrice) + "0";
		// 单独计算菜品总价
		String totalPrice1 = fastfoodaction.getFastCommodityPrice(Constants.DISH_WEIGHT2,
				dishNamePrice[1].substring(1));

		// 关闭菜品编辑框
		solo.clickOnView(ID.COMMODITYPRICE_COFIRM_BUTTON);
		solo.sleep(Constants.SLEEPONE);
		// 输入整单备注
		solo.clickOnView(fastFoodId.SETMORE);
		solo.clickOnView(fastFoodId.SETREMARK);
		EditText editTextDemo = (EditText) solo.getView(fastFoodId.EDITMEMOTEXT);
		solo.enterText(editTextDemo, Constants.COMMON_DEMO_TEXT1);

		// 获取输入的整单备注
		String demoZD = solo.getTextStr(fastFoodId.EDITMEMOTEXT);
		solo.clickOnView(fastFoodId.BTNCLOSE);
		solo.sleep(Constants.SLEEPTHREE);

		// 验证购物车
		fastfoodaction.assertShoppingCartForCommodityPriceDish(1, dishNamePrice[0], Constants.DISH_WEIGHT2, totalPrice1,
				cpf[0], addPrice[0], cpf[1], addPrice[1], cpf[2], addPrice[2], fe, dFeedPrice);
		// 验证备注-单菜品备注
		assertEquals("Step5:购物车单菜品备注有误", demoD, fastfoodaction.getShoppingCartDemoD());

		// 验证-整单备注
		assertEquals("Step6:购物车整单备注有误", demoZD, fastfoodaction.getShoppingCartDemoZ());

	}

	/*
	 * 称重、变价-点单-购物车
	 */
	@Test
	public void test002OrderDish() throws Exception {
		// 进入购物车，点击变|称 中类
		action.selectCommodityClassification(Constants.CHANGEPRICE);
		String[] dishNamePrice = action.getDishNamePrice(0);
		// 选择 菜品 龙须菜
		action.selectDish(0);
		// 输入龙须菜的重量 2.5
		action.inputWeight(Constants.DISH_WEIGHT1);
		// 验证龙须菜的数量显示和 总价显示
		assertEquals("Step1:数量不一致", Constants.DISH_WEIGHT1, solo.getTextStr(ID.DISH_COUNT_EDIT));
		// 计算单行菜品总价
		String dishPrice = fastfoodaction.getFastCommodityPrice(Constants.DISH_WEIGHT1, dishNamePrice[1].substring(1));
		assertEquals("Step2:金额不一致", dishPrice, solo.getTextStr(fastFoodId.DISHPRICE));
		// 修改龙须菜的 数量
		EditText editText = (EditText) solo.getView(ID.DISH_COUNT_EDIT);
		solo.clearEditText(editText);
		solo.enterText(editText, Constants.DISH_WEIGHT2);
		solo.clickOnView(fastFoodId.TV_DISH_PRICE);
		// 修改价格
		EditText priceEditText = (EditText) solo.getView(ID.ENTER_PRICE);
		solo.clearEditText(priceEditText);
		solo.enterText(priceEditText, Constants.PRICE1);
		solo.clickOnView(ID.CLOSE_DISCOUNT);
		// 验证龙须菜的数量显示和 总价显示

		assertEquals("Step3:数量不一致", Constants.DISH_WEIGHT2, solo.getTextStr(fastFoodId.DISHNUM));
		String dishPrice01 = fastfoodaction.getFastCommodityPrice(Constants.DISH_WEIGHT2, Constants.PRICE1);
		assertEquals("Step4:菜品金额不一致", dishPrice01, solo.getTextStr(fastFoodId.DISHPRICE));

		// 验证 购物车总价格显示
		assertEquals("Step5:菜品总金额不一致", dishPrice01, solo.getTextStr(fastFoodId.FASTFOODCASH).substring(3));

	}

	/**
	 * 非称重商品（类别、做法、口味、加料）-点单-购物车
	 */
	@Test
	public void test003OrderDish() throws Exception {
		// 选择中类
		action.selectCommodityClassification(0);
		String[] dishNamePrice = action.getDishNamePrice(1);
		// 选择非称重商品
		action.selectDish(1);
		ListView listView = (ListView) solo.getView(fastFoodId.LISTVIEW_SELECTED_DISH);
		View view = listView.getChildAt(0);
		solo.clickOnView(view);
		// 点非称重商品 2份
		solo.clickOnView(fastFoodId.TEXT_ADD);
		// 获取输入框的数量
		String dishNum = solo.getTextStr(ID.DISH_COUNT_EDIT);
		// 加类别、做法、口味、加料各一份
		int index1[] = action.getCategoryIndexScope();
		int index2[] = action.getPracticeIndexScope();
		int index3[] = action.getFlavorIndexScope();
		action.setCommodityPriceDishCPF_Weight1(index1[0], 0, index2[0], 0, index3[0], 0);

		String cpf[] = action.getSelectedCategoryPracticeFlavor();
		String cpfPrice[] = action.getSelectedCategoryPracticeFlavorPrice();
		// 获取选择的类别、做法、口味名称 addTexts【0】类别 addTexts【1】 做法 addTexts【2】口味
		String[] addTexts = action.getSelectedCategoryPracticeFlavor();
		// 获取选择的类别、做法、口味的价格
		String[] addTextsPrice = action.getSelectedCategoryPracticeFlavorPrice();
		// 计算选择的 类别、做法、口味的 价格（原菜品数量*类别单价 /做法单价/口味单价）
		String[] addPrice = fastfoodaction.getFastAddPrice(Constants.DISH_COUNT2, addTextsPrice[0], addTextsPrice[1],
				addTextsPrice[2]);
		float tFeedPrice = 0;
		for (int i = 0; i < addPrice.length; i++) {
			tFeedPrice += Float.parseFloat(addPrice[i].substring(1));
		}

		// 选择加料
		action.selectFeed(1, 0);
		action.selectFeed(1, 1);
		// 计算加料总金额

		String fePrice[] = action.getSelectedFeedPrice();
		String fe = action.getSelectedFeed();
		float feedPrice = 0;
		for (int i = 0; i < fePrice.length; i++) {
			feedPrice += Float.parseFloat(fePrice[i].substring(2));
		}
		float dFeedPrice = feedPrice * Float.parseFloat(Constants.DISH_COUNT2);
		String ddFeedPrice = "￥" + String.valueOf(dFeedPrice) + "0";

		String dishPrice = fastfoodaction.getFastCommodityPrice(dishNum, dishNamePrice[1].substring(1));

		// 验证 非称重商品的 数量和 金额、类别、做法、口味、加料 的名称 数量 金额
		fastfoodaction.assertShoppingCartForCommodityPriceDish(1, dishNamePrice[0], Constants.DISH_COUNT2, dishPrice,
				cpf[0], addPrice[0], cpf[1], addPrice[1], cpf[2], addPrice[2], fe, ddFeedPrice);

		// 验证收银总价
		float totalPrice = dFeedPrice + Float.parseFloat(dishPrice.substring(1)) + tFeedPrice;
		assertEquals("Step1:菜品总金额不一致", String.valueOf(totalPrice) + "0",
				solo.getTextStr(fastFoodId.FASTFOODCASH).substring(4));
		solo.clickOnView(ID.CLOSE_BUTTON);
		// 修改非称重商品 为 3份
		solo.sleep(Constants.SLEEPTWO);
		solo.clickOnView(view);
		EditText editText = (EditText) solo.getView(ID.DISH_COUNT_EDIT);
		solo.clearEditText(editText);
		solo.enterText(editText, Constants.DISH_COUNT5);
		// 获取输入框的数量
		String dishNum01 = solo.getTextStr(ID.DISH_COUNT_EDIT);
		solo.sleep(Constants.SLEEPTWO);
		solo.clickOnView(ID.CLOSE_BUTTON);
		// 计算选择的 类别、做法、口味的 价格（原菜品数量*类别单价 /做法单价/口味单价）
		String[] addPrice01 = fastfoodaction.getFastAddPrice(Constants.DISH_COUNT5, addTextsPrice[0], addTextsPrice[1],
				addTextsPrice[2]);
		// 计算加料的金额
		float dFeedPrice01 = feedPrice * Float.parseFloat(Constants.DISH_COUNT5);
		String ddFeedPrice01 = "￥" + String.valueOf(dFeedPrice01) + "0";
		// 计算单行菜品总价
		String dishPrice01 = fastfoodaction.getFastCommodityPrice(dishNum01, dishNamePrice[1].substring(1));
		// 验证 非称重商品的 数量和 金额、类别、做法、口味、加料 的名称 数量 金额
		fastfoodaction.assertShoppingCartForCommodityPriceDish(1, dishNamePrice[0], Constants.DISH_COUNT5, dishPrice01,
				cpf[0], addPrice01[0], cpf[1], addPrice01[1], cpf[2], addPrice01[2], fe, ddFeedPrice01);

		// 验证收银总金额
		float totalPrice01 = totalPrice * Float.valueOf(Constants.DISH_COUNT5) / Float.valueOf(Constants.DISH_COUNT2);
		assertEquals("Step2:菜品总金额不一致", String.valueOf(totalPrice01) + "0",
				solo.getTextStr(fastFoodId.FASTFOODCASH).substring(4));

	}
	
	

}
