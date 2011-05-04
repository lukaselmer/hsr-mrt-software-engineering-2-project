class UsersController < ApplicationController
  before_filter :authorize_secretary!, :only => [:new, :create, :edit, :update, :destroy]
  
  # GET /users
  def index
    @users = User.all

    respond_to do |format|
      format.html # index.html.erb
    end
  end

  # GET /users/1
  def show
    @user = User.find(params[:id])
  end

  # GET /users/new
  def new
    @user = User.new
  end

  # GET /users/1/edit
  def edit
    @user = User.find(params[:id])
  end

  # POST /users
  def create
    @user = User.new(params[:user])
    @user.type = params[:user][:type]

    if @user.save
      redirect_to(user_path(@user), :notice => User.model_name.human + ' ' + t(:create_successful))
    else
      render :action => "new"
    end
  end

  # PUT /users/1
  def update
    @user = User.find(params[:id])
    @user.type = params[:user][:type]

    if @user.update_attributes(params[:user])
      redirect_to(user_path(@user), :notice => User.model_name.human + ' ' + t(:update_successful))
    else
      render :action => "edit"
    end
  end

  # DELETE /users/1
  def destroy
    @user = User.find(params[:id])
    @user.destroy

    redirect_to(users_url)
  end
end
