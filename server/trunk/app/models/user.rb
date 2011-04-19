class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :encryptable, :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :rememberable
  
  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :remember_me, :first_name, :last_name, :type, :password_salt

end