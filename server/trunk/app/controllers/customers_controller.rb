class CustomersController < ApplicationController
  # GET /customers
  def index
    @customers = Customer.all

    respond_to do |format|
      format.html # index.html.erb
    end
  end

  # GET /customers/1
  def show
    @customer = Customer.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
    end
  end

  # GET /customers/new
  def new
    @customer = Customer.new

    respond_to do |format|
      format.html # new.html.erb
    end
  end

  # GET /customers/1/edit
  def edit
    @customer = Customer.find(params[:id])
  end

  # POST /customers
  def create
    @customer = Customer.new(params[:customer])

    respond_to do |format|
      if @customer.save
        format.html { redirect_to(@customer, :notice => 'Customer was successfully created.') }
      else
        format.html { render :action => "new" }
      end
    end
  end

  # PUT /customers/1=
  def update
    @customer = Customer.find(params[:id])

    respond_to do |format|
      if @customer.update_attributes(params[:customer])
        format.html { redirect_to(@customer, :notice => 'Customer was successfully updated.') }
      else
        format.html { render :action => "edit" }
      end
    end
  end

  # DELETE /customers/1
  def destroy
    @customer = Customer.find(params[:id])
    @customer.destroy

    respond_to do |format|
      format.html { redirect_to(customers_url) }
    end
  end

  def synchronize
    if params[:last_update].nil?
      @updated_customers = Customer.all
    else
      @updated_customers = Customer.where("updated_at > :last_update OR created_at > :last_update OR deleted_at > :last_update", :last_update => params[:last_update])
    end
    
    respond_to do |format|
        format.json { render :json => @updated_customers }
    end
  end
end
