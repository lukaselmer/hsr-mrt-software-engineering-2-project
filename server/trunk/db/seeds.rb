# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ :name => 'Chicago' }, { :name => 'Copenhagen' }])
#   Mayor.create(:name => 'Daley', :city => cities.first)
User.delete_all
User.create!(:first_name => "Sec", :last_name => "Retary", :email => 'secretary@mrt.ch', :password => 'mrt', :user_type => User::TYPES[:SECRETARY])
User.create!(:first_name => "Field", :last_name => "Worker", :email => 'field_worker@mrt.ch', :password => 'mrt', :user_type => User::TYPES[:FIELD_WORKER])

"Waldemar Lamprecht, Otto Traugott, Hermann Elmo, Veit Ingolf, Björn Simon, 
Klaus Dietfried, Gotthilf Wieland, June Gretchen, Taryn Shelia, Tarah Dione, 
Angelica Xavia, Demontongue Catbroom, Silverbeam Blackmoon, Whirl Gnaw, Nike Medb, 
Pia Ligeia, Ásdís Eirene, Beatrix Afra, Rochus Rocco, Eirene Felicianus, Hrodger Amantius, 
Sigimund Lóegaire, Friðþjófr Ramessu, Yima Philander, Enlil Prabhu, Owain Iovis, Týr Cronus, 
Partha Pramoda, Partha Pramoda, Gawain Jarl, Cupid Amulius".split(',').each do |name|
  f, l = name.strip.split(' ')
  Customer.create!(:fist_name => f.strip, :last_name => l.strip)
end

