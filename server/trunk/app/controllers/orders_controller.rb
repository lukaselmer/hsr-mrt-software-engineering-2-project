class OrdersController < ApplicationController
  # GET /orders
  def index
    @orders = Order.all

    respond_to do |format|
      format.html # index.html.erb
    end
  end

  # GET /orders/1
  # GET /orders/1.xml
  def show
    @order = Order.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
    end
  end

  # GET /orders/new
  def new
    @order = Order.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @order }
    end
  end

  # GET /orders/1/edit
  def edit
    @order = Order.find(params[:id])
  end

  # POST /orders
  def create
    @order = Order.new(params[:order])
    @customers = Customer.all

    respond_to do |format|
      if @order.save
        format.html { redirect_to(@order, :notice => Order.model_name.human + ' ' + t(:create_successful)) }
      else
        format.html { render :action => "new" }
      end
    end
  end

  # PUT /orders/1
  def update
    @order = Order.find(params[:id])
    @customers = Customer.all

    respond_to do |format|
      if @order.update_attributes(params[:order])
        format.html { redirect_to(@order, :notice => Order.model_name.human + ' ' + t(:update_successful)) }
      else
        format.html { render :action => "edit" }
      end
    end
  end

  # DELETE /orders/1
  def destroy
    @order = Order.find(params[:id])
    @order.destroy

    respond_to do |format|
      format.html { redirect_to(orders_url) }
    end
  end
end
