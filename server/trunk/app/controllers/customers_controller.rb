class CustomersController < ApplicationController
  before_filter :authorize_secretary!, :only => [:new, :create, :edit, :update, :destroy]

  # GET /customers
  def index
    @customers = Customer.all
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

    if @customer.save
      redirect_to(@customer, :notice => 'Customer was successfully created.')
    else
      render :action => "new"
    end
  end

  # PUT /customers/1=
  def update
    @customer = Customer.find(params[:id])

    if @customer.update_attributes(params[:customer])
      redirect_to(@customer, :notice => 'Customer was successfully updated.')
    else
      render :action => "edit"
    end
  end

  # DELETE /customers/1
  def destroy
    @customer = Customer.find(params[:id])
    @customer.destroy

    redirect_to(customers_url)
  end

  def synchronize
    @updated_customers = Customer.updated_after(params[:last_update])
    render :json => @updated_customers
  end
end
