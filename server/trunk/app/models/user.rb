class User < ActiveRecord::Base
  TYPES = { :ADMIN => 1, :SECRETARY => 2, :EMPLOYEE => 3 }

  # Include default devise modules. Others available are:
  # :token_authenticatable, :encryptable, :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :rememberable
  
  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :remember_me, :first_name, :last_name, :user_type, :password_salt
  validates :password, :presence => true, :on => :create
end