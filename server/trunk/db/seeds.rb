# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ :name => 'Chicago' }, { :name => 'Copenhagen' }])
#   Mayor.create(:name => 'Daley', :city => cities.first)
User.delete_all
User.create!(:email => 'admin@mrt.ch', :password => 'mrt', :user_type => User::TYPES[:ADMIN])
User.create!(:email => 'secretary@mrt.ch', :password => 'mrt', :user_type => User::TYPES[:SECRETARY])
User.create!(:email => 'worker@mrt.ch', :password => 'mrt', :user_type => User::TYPES[:FIELD_WORKER])
